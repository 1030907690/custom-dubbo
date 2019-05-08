package com.zzq.test.handler;

import com.zzq.test.parser.UserBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

public class MyNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("user",new UserBeanDefinitionParser());
    }

 }
