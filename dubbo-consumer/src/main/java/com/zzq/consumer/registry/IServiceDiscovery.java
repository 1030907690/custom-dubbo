package com.zzq.consumer.registry;

public interface IServiceDiscovery {

    /**
     * 服务发现
     * **/
  String discovery(String serviceName)  ;
}
