package com.zzq.consumer;

import com.zzq.consumer.customtag.pojo.Dubbo;
import com.zzq.consumer.registry.IServiceDiscovery;
import com.zzq.consumer.registry.ServiceDiscoveryImpl;
import com.zzq.consumer.scan.ClassPathMapperScanner;
import com.zzq.consumer.scan.CustomBeanFactoryPostProcessor;
import com.zzq.provider.api.ITestService;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: 消费者启动 - 第二版改动
 * @date 2019/2/11 17:35
 */
public class StartConsumer {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-base.xml");

        String[] beanNames = context.getBeanDefinitionNames();

        for (String beanName :  beanNames) {
            System.out.println("beanName: "+ beanName);
        }

        Dubbo dubbo = context.getBean(Dubbo.class);

        IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl(dubbo.getAddress());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscovery);
        ITestService testService = rpcClientProxy.create(ITestService.class);
        //System.out.println("testService "+ testService);
        String obj = testService.test("hello ");
        System.out.println(obj);
    }
}
