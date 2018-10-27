package com.zzq.consumer;

import com.zzq.consumer.registry.IServiceDiscovery;
import com.zzq.consumer.registry.ServiceDiscoveryImpl;
import com.zzq.provider.api.ITestService;

public class TestClient {

    public static void main(String[] args) {
        IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscovery);
        ITestService testService = rpcClientProxy.create(ITestService.class);
        //System.out.println("testService "+ testService);
        String obj = testService.test("hello ");
        System.out.println(obj);
    }

}
