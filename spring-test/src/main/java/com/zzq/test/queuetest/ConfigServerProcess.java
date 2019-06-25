package com.zzq.test.queuetest;

import com.zzq.test.queue.IZookeeperWatcherNotify;
import com.zzq.test.queue.ZookeeperListenser;
import com.zzq.test.utils.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: 关于读取配置的 队列处理
 * @date 2019/6/18 16:44
 */
@Qualifier(value = "configServerProcess")
@Component
public class ConfigServerProcess implements IZookeeperWatcherNotify {

    private static final Logger log = LoggerFactory.getLogger(ConfigServerProcess.class);


    /**
     * 配置
     **/
    private final Map<String, String> config = new ConcurrentHashMap<>();

    @Resource
    private ZookeeperListenser zookeeperListenser;
/*
    @Resource
    private MongoDAO mongoDAO;*/

    /***
     * zhouzhongqing
     * 2019年6月24日17:20:24
     * 得到配置
     * @param key
     * @param defaultValue
     * */
    public String getConfigOrDefault(String key, String defaultValue) {
        String value = config.getOrDefault(key, null);
        if (null == value) {

            //如果没有走zk取
            try {
                value = new String(zookeeperListenser.getCuratorFramework().getData().forPath(ZookeeperListenser.ROOT_PATH + Constants.CONFIG_SERVER_NODE_LISTENER + key));
            } catch (Exception e) {
                System.out.println(" zk没获取到数据  "+ key);
            }

           /* if (null == value) {
                //如果还是空 到数据库取
                SysConfig sysConfig = mongoDAO.findByOne(SysConfig.class, "itemCode", key);
                if (null != sysConfig && null != sysConfig.getItemVal()) {
                    value = sysConfig.getItemVal();
                }
            }*/

            if(null == value) {
                // 如果找不到使用默认值
                value = defaultValue;
            }

        }
        return value;
    }

    @Override
    public void process(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        String data = new String(treeCacheEvent.getData().getData());
        if (null != data) {
            String key = treeCacheEvent.getData().getPath().replace(ZookeeperListenser.ROOT_PATH + Constants.CONFIG_SERVER_NODE_LISTENER, "");
            //更新配置
            config.put(key, data);
            System.out.println("更新 [" + key + "]" + " 为 [" + data + "]");
        }
    }

    @Override
    public boolean supports(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        boolean isSupports = false;
        switch (treeCacheEvent.getType()) {
            case NODE_ADDED:
                //不是根节点才处理
                if (!ZookeeperListenser.ROOT_PATH.equals(treeCacheEvent.getData().getPath())) {
                    if (null != treeCacheEvent.getData()) {
                        //判断节点是否需要处理
                        if (treeCacheEvent.getData().getPath().startsWith(ZookeeperListenser.ROOT_PATH + Constants.CONFIG_SERVER_NODE_LISTENER)) {
                            isSupports = true;
                        }
                    }
                }
                break;
            case NODE_UPDATED:
                //不是根节点才处理
                if (!ZookeeperListenser.ROOT_PATH.equals(treeCacheEvent.getData().getPath())) {
                    if (null != treeCacheEvent.getData()) {
                        //判断节点是否需要处理
                        if (treeCacheEvent.getData().getPath().startsWith(ZookeeperListenser.ROOT_PATH + Constants.CONFIG_SERVER_NODE_LISTENER)) {
                            isSupports = true;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return isSupports;
    }

    public static void main(String[] args) {
        String data = "{\"type\":0,\"userId\":1212,\"channelId\":\"null\",\"userType\":0}";
        //ReceiveMessage receiveMessage = JSONObject.parseObject(data, ReceiveMessage.class);

    }

}