package com.zzq.test.queuetest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: zookeeper队列的测试  可以拿来做分布式队列  也可以做一些简单的<key,value> 配置中心
 * @date 2019/6/25 15:19
 */
public class StartQueue {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-base.xml");
        System.out.println(" 已注册的bean : ");
        for (String s : context.getBeanDefinitionNames()) {
            System.out.println(s);
        }
    }
}
