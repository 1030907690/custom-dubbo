package com.zzq.consumer.customtag.handler;

import com.zzq.consumer.customtag.parser.DubboBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class DubboNamespaceHandler extends NamespaceHandlerSupport {


    @Override
    public void init() {
        registerBeanDefinitionParser("dubbo",new DubboBeanDefinitionParser());
    }
}
