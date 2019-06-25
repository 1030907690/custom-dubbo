package com.zzq.test.queue;

import com.zzq.test.queue.configuration.GenericConfiguration;
import com.zzq.test.utils.SnowFlake;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: 监听目录
 * @date 2019/6/18 19:01
 */
@Component
public class ZookeeperListenser implements InitializingBean {

    private final String ENCODE = "UTF-8";
    private final SnowFlake snowFlake = new SnowFlake(1, 2);
    public static final String ROOT_PATH = "/queue_fifo";


    private CuratorFramework curatorFramework;

    private final CountDownLatch cdl = new CountDownLatch(1);

    @Resource
    private ExecutorService executorService;
    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private GenericConfiguration genericConfiguration;

    private Map<String, IZookeeperWatcherNotify> handler;

    private void initialize() throws Exception {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(genericConfiguration.getZookeeperAddress()).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        curatorFramework.start();


        //判断节点是否存在  不存在则创建
        if (null == curatorFramework.checkExists().forPath(ROOT_PATH)) {
            //创建
            curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT).forPath(ROOT_PATH, "0".getBytes());

        }

        final TreeCache treeCache = new TreeCache(curatorFramework, ROOT_PATH);
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                handler.forEach((k, v) -> {
                    if(v.supports(curatorFramework,treeCacheEvent)) {
                        //处理回调
                        v.process(curatorFramework, treeCacheEvent);
                    }
                });
         /*switch (treeCacheEvent.getType()) {
                    case NODE_ADDED:
                        System.out.println("Node add " + ZKPaths.getNodeFromPath(treeCacheEvent.getData().getPath()));
                        break;
                    case NODE_REMOVED:
                        System.out.println("Node removed " + ZKPaths.getNodeFromPath(treeCacheEvent.getData().getPath()));
                        break;
                    case NODE_UPDATED:
                        System.out.println("Node updated " + ZKPaths.getNodeFromPath(treeCacheEvent.getData().getPath()));
                        break;
                }*/
/*
                System.out.println( "类型"+ treeCacheEvent.getType());
                if (null != treeCacheEvent.getData()) {
                    System.err.println(treeCacheEvent.getData().getPath());
                    System.err.println(new String(treeCacheEvent.getData().getData()));
                } else {
                    System.err.println("getData null");
                }*/

            }
        });
        treeCache.start();

    }


    ;     //创建
      /*  curatorFramework.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL).forPath("/queue_fifo/"+UUID.randomUUID().toString(), "0".getBytes());
*/


    /**
     * zhouzhongqing
     * 2019年6月18日22:59:41
     * 添加数据到队列
     **/
    public void offset(String data) {
        try {
            curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL).forPath(ROOT_PATH + "/" + String.valueOf(snowFlake.nextId()), data.getBytes(ENCODE));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * zhouzhongqing
     * 2019年6月18日22:59:41
     * 添加数据到队列
     * @param path
     * @param data
     **/
    public void offset(String path, String data) {
        try {
            curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL).forPath(ROOT_PATH + path, data.getBytes(ENCODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handler = applicationContext.getBeansOfType(IZookeeperWatcherNotify.class);
        if (null == handler) {
            handler = new HashMap<>();
        }

        //初始化zookeeper 连接
        executorService.execute(() -> {
            try {
                initialize();
                cdl.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }
}
