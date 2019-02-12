package com.zzq.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: 提供者启动
 * @date 2019/2/11 17:35
 */
public class StartProvider {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-base.xml");

        String[] beanNames = context.getBeanDefinitionNames();

        for (String beanName :  beanNames) {
            System.out.println("beanName: "+ beanName);
        }

    }
}
