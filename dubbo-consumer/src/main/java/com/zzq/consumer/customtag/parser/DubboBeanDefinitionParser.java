package com.zzq.consumer.customtag.parser;

import com.zzq.consumer.bean.MapperBean;
import com.zzq.consumer.customtag.pojo.Dubbo;
import com.zzq.consumer.registry.IServiceDiscovery;
import com.zzq.consumer.registry.ServiceDiscoveryImpl;
import com.zzq.provider.util.PkgUtil;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.util.Set;

public class DubboBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	// element对应的类
	@Override
	protected Class<?> getBeanClass(Element element) {
		return Dubbo.class;
	}

	// 从element中解析并提取对应的元素
/*	@Override
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



	}*/

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
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
		//扫描service
		//this.registerBeanDefinition(holder, parserContext.getRegistry());
		BeanDefinitionRegistry registry = parserContext.getRegistry();

		IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl(address);

		Set<Class<?>> sets = PkgUtil.getClzFromPkg("com.zzq.provider.api");
		sets.forEach((k)->{
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(MapperBean.class);
			MutablePropertyValues values = beanDefinition.getPropertyValues();
			values.addPropertyValue("serviceDiscovery",serviceDiscovery);
			values.addPropertyValue("mapperInterface", k);
			/*String beanName = BeanDefinitionReaderUtils.generateBeanName(
					beanDefinition, registry, true);
*/
			String simpleName = k.getSimpleName();
			String beanName = simpleName;
			if("I".equals(simpleName.substring(0,1))){
				beanName = simpleName.substring(1);
			}

			beanName = beanName.substring(0,1).toLowerCase() + beanName.substring(1);

			System.out.println("registerBeanDefinition  " + beanName);
			//将testService注册进入spring容器 ,其实真正调用时是FactoryBean里面的代理对象
			registry.registerBeanDefinition(beanName, beanDefinition );

		});

	}

}
