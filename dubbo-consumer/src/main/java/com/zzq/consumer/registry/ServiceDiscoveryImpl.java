package com.zzq.consumer.registry;

import com.zzq.consumer.loadbalance.ILoadBalance;
import com.zzq.consumer.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

public class ServiceDiscoveryImpl implements IServiceDiscovery{

    private List<String> repos = null;
    private CuratorFramework curatorFramework;

    public ServiceDiscoveryImpl(){
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZKConfig.CONNECTION_STR).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,10)).build();
        curatorFramework.start();

    }

    public ServiceDiscoveryImpl(String zkAddress){
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkAddress).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,10)).build();
        curatorFramework.start();

    }

    @Override
    public String discovery(String serviceName)     {
        String path = ZKConfig.ZK_REGISTER_PATH+"/"+serviceName;

        try{
            repos = curatorFramework.getChildren().forPath(path);
            //动态感知节点变化  zk监听机制
            registerWatch(path);

            ILoadBalance loadBalance = new RandomLoadBalance();
            return loadBalance.select(repos);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 动态感知 监听机制
     * **/
    private void registerWatch(String path){
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                repos = curatorFramework.getChildren().forPath(path);
            }
        };
        childrenCache.getListenable().addListener(pathChildrenCacheListener);

        try {
            childrenCache.start();
        }catch (Exception e){
            System.err.println("registerWatch error");
            e.printStackTrace();
        }

    }
}
