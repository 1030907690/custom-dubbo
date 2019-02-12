package com.zzq.consumer;

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



    }
}
