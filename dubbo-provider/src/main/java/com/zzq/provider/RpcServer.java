package com.zzq.provider;

import com.zzq.provider.annotation.RpcAnnotation;
import com.zzq.provider.registry.IRegistryCenter;

import java.util.HashMap;
import java.util.Map;

public class RpcServer {


    private IRegistryCenter registryCenter;

    private String serviceAddress;

    private Map<String,Object> handlerMap = new HashMap<>();

    public RpcServer(IRegistryCenter registryCenter ,String serviceAddress){
        this.registryCenter = registryCenter;
        this.serviceAddress = serviceAddress;
    }


    public void bind(Object ... services){
        for (Object service : services){
            RpcAnnotation rpcAnnotation = service.getClass().getAnnotation(RpcAnnotation.class);
            String serviceName = rpcAnnotation.value().getName();
            handlerMap.put(serviceName,service);
        }
    }

    //发布服务和监听端口
    public void publisher(){

        for (String serviceName:handlerMap.keySet()){
            registryCenter.register(serviceName,serviceAddress);
        }
    }


}
