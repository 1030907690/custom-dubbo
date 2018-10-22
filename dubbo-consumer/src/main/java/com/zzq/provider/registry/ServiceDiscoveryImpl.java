package com.zzq.provider.registry;

import com.zzq.provider.loadbalance.ILoadBalance;
import com.zzq.provider.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import javax.annotation.PostConstruct;
import java.util.List;

public class ServiceDiscoveryImpl implements IServiceDiscovery{

    private List<String> repos = null;
    private CuratorFramework curatorFramework;

    public ServiceDiscoveryImpl(String address){
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZKConfig.CONNECTION_STR).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,10)).build();
        curatorFramework.start();

    }

    @Override
    public String discovery(String serviceName)     {
        String path = ZKConfig.ZK_REGISTER_PATH+"/"+serviceName;

        try{
            repos = curatorFramework.getChildren().forPath(path);
            //动态感知节点变化  zk监听机制


            ILoadBalance loadBalance = new RandomLoadBalance();
            return loadBalance.select(repos);
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }
}
