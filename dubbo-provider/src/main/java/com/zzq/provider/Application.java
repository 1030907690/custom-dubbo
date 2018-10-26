package com.zzq.provider;

import com.zzq.provider.registry.IRegistryCenter;
import com.zzq.provider.registry.RegistryCenterImpl;
/**
 * 测试客户端
 * **/
public class Application {

    public static void main(String[] args) throws Exception{

        IRegistryCenter registryCenter = new RegistryCenterImpl();
        registryCenter.register("com.zzq.provider.ITestService","127.0.0.1:9090");
        System.in.read();
        System.out.println(ITestService.class.getSimpleName());
    }
}
