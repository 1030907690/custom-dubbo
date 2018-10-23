package com.zzq.provider;

import com.zzq.provider.registry.IRegistryCenter;
import com.zzq.provider.registry.RegistryCenterImpl;

public class ServerTest {
    public static void main(String[] args) {
        ITestService testService = new TestServiceImpl();
        IRegistryCenter registryCenter = new RegistryCenterImpl();

        //服务发布,监听端口
        RpcServer rpcServer = new RpcServer(registryCenter,"127.0.0.1:8080");

         //一个服务名称可能对应多个不同实现
        //服务名称和实例对象的关系
        rpcServer.bind(testService);

        rpcServer.publisher();

    }

}
