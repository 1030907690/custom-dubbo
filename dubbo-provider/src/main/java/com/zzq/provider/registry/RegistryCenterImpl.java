package com.zzq.provider.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/***
 * 注册到zk的service impl
 * */
public class RegistryCenterImpl implements IRegistryCenter {

    private CuratorFramework curatorFramework;


    {

        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZKConfig.CONNECTION_STR).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,10)).build();
        curatorFramework.start();
    }


    public void register(String serviceName, String serviceAddress) {
        //操作zk api

        //增加节点
        String servicePath = ZKConfig.ZK_REGISTER_PATH+"/"+serviceName;
        try {
            //判断节点是否存在  不存在则创建
            if(null == curatorFramework.checkExists().forPath(servicePath)){
                //创建
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath(servicePath,"0".getBytes());

            }

            //有了节点后，对应接口的url
            //服务发布的地址
            String addressPath = servicePath+"/"+serviceAddress;
            String rsNode = curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(addressPath,"0".getBytes());

            System.out.print("service push success "+ rsNode);



        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
