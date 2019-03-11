package com.zzq.consumer.configuration;

import com.zzq.consumer.RpcClientProxy;
import com.zzq.consumer.customtag.pojo.Dubbo;
import com.zzq.consumer.registry.IServiceDiscovery;
import com.zzq.consumer.registry.ServiceDiscoveryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/3/5 16:44
 */
@Configuration
//@Import(ServiceScannerConfigurer.class)
public class AppConfig {


    @Resource
    private Dubbo dubbo;

    @Bean
    public IServiceDiscovery serviceDiscovery(){
        System.out.println(" dubbo.getAddress() " +dubbo.getAddress());
        return  new ServiceDiscoveryImpl(dubbo.getAddress());
    }






}
