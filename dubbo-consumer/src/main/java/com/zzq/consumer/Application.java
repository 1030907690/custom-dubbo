package com.zzq.consumer;

import com.zzq.consumer.registry.IServiceDiscovery;
import com.zzq.consumer.registry.ServiceDiscoveryImpl;
import com.zzq.provider.api.ITestService;

public class Application {

    public static void main(String[] args) throws Exception{

        IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl();
        String url = serviceDiscovery.discovery("com.zzq.provider.ITestService");

        System.out.println(url+"--"+ITestService.class.getName());
    }
}
