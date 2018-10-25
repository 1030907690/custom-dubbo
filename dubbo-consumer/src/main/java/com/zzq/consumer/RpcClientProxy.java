package com.zzq.consumer;

import com.zzq.consumer.registry.IServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy {

    private IServiceDiscovery serviceDiscovery;

    public RpcClientProxy(IServiceDiscovery serviceDiscovery){
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(Class<T> interfaceClass){
        if(null != interfaceClass){
            return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                            //封装对象
                            RpcRequest request = new RpcRequest();
                            request.setClassName(method.getDeclaringClass().getName());
                            request.setMethodName(method.getName());
                            request.setTypes(method.getParameterTypes());
                            request.setParams(args);

                            //服务发现
                            String serviceName = interfaceClass.getName();
                            //得到地址
                            String serviceAddress = serviceDiscovery.discovery(serviceName);

                            //解析ip和port
                            String [] address = serviceAddress.split(":");
                            String host = address[0];
                            int port = Integer.parseInt(address[1]);

                            //socket netty连接

                            final RpcProxyHandler rpcProxyHandler = new RpcProxyHandler();
                            return null;
                        }
                    });
        }
        return null;
    }
}
