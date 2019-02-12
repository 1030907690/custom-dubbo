package com.zzq.consumer.customtag.parser;

import com.zzq.consumer.customtag.pojo.Dubbo;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class DubboBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	// element对应的类
	@Override
	protected Class<?> getBeanClass(Element element) {
		return Dubbo.class;
	}

	// 从element中解析并提取对应的元素
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String id = element.getAttribute("id");
		String address = element.getAttribute("address");
		String name = element.getAttribute("name");
		if (StringUtils.hasText(id)) {
			builder.addPropertyValue("id", id);
		}
		if (StringUtils.hasText(address)) {
			builder.addPropertyValue("address", address);
		}
		if (StringUtils.hasText(name)) {
			builder.addPropertyValue("name", name);
		}
		System.out.println("id :" + id +" name : " +name +"  address ：" +address);
	}
}
