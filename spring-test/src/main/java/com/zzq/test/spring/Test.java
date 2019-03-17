package com.zzq.test.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-base.xml");

        String[] beanNames = context.getBeanDefinitionNames();

        for (String beanName :  beanNames) {
            System.out.println("beanName: "+ beanName);
        }

    }
}
