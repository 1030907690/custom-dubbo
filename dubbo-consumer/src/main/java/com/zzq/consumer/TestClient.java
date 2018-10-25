package com.zzq.consumer;

import com.zzq.consumer.registry.IServiceDiscovery;
import com.zzq.consumer.registry.ServiceDiscoveryImpl;

public class TestClient {

    public static void main(String[] args) {
        IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl();

    }

}
