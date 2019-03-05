package com.zzq.consumer.bean;

import com.zzq.consumer.RpcClientProxy;
import com.zzq.consumer.registry.IServiceDiscovery;
import com.zzq.provider.api.ITestService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/3/5 17:04
 */
public class MapperBean  implements FactoryBean  {

    private IServiceDiscovery serviceDiscovery;
    private Class mapperInterface;
    @Nullable
    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Nullable
    @Override
    public Object getObject() throws Exception {
        return new RpcClientProxy(serviceDiscovery).create(mapperInterface);
    }

    public IServiceDiscovery getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void setServiceDiscovery(IServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public Class getMapperInterface() {
        return mapperInterface;
    }

    public void setMapperInterface(Class mapperInterface) {
        this.mapperInterface = mapperInterface;
    }
}
