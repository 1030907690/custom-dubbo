package com.zzq.test.handler;

import com.zzq.test.parser.UserBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport {


    public void init() {
        registerBeanDefinitionParser("user",new UserBeanDefinitionParser());
    }
}
