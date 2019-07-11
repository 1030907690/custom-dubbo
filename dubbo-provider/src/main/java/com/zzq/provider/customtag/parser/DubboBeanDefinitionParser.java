package com.zzq.provider.customtag.parser;

import com.zzq.provider.RpcServer;
import com.zzq.provider.customtag.pojo.Dubbo;
import com.zzq.provider.registry.IRegistryCenter;
import com.zzq.provider.registry.RegistryCenterImpl;
import com.zzq.provider.util.PkgUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DubboBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	// element对应的类
	@Override
	protected Class<?> getBeanClass(Element element) {
		return Dubbo.class;
	}

	// 从element中解析并提取对应的元素
	/*@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String id = element.getAttribute("id");
		String address = element.getAttribute("address");
		String name = element.getAttribute("name");
		String protocol = element.getAttribute("protocol");
		String basePackage = element.getAttribute("basePackage");
		String timeout = element.getAttribute("timeout");
		String port = element.getAttribute("port");
		if (StringUtils.hasText(id)) {
			builder.addPropertyValue("id", id);
		}
		if (StringUtils.hasText(address)) {
			builder.addPropertyValue("address", address);
		}
		if (StringUtils.hasText(name)) {
			builder.addPropertyValue("name", name);
		}

		if (StringUtils.hasText(protocol)) {
			builder.addPropertyValue("protocol", protocol);
		}

		if (StringUtils.hasText(basePackage)) {
			builder.addPropertyValue("basePackage", basePackage);
		}

		if (StringUtils.hasText(timeout)) {
			builder.addPropertyValue("timeout", timeout);
		}

		if (StringUtils.hasText(port)) {
			builder.addPropertyValue("port", port);
		}
		System.out.println("id :" + id +" name : " +name +"  address ：" +address);
	}*/


	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String id = element.getAttribute("id");
		String address = element.getAttribute("address");
		String name = element.getAttribute("name");
		String protocol = element.getAttribute("protocol");
		String basePackage = element.getAttribute("basePackage");
		String timeout = element.getAttribute("timeout");
		String port = element.getAttribute("port");
		if (StringUtils.hasText(id)) {
			builder.addPropertyValue("id", id);
		}
		if (StringUtils.hasText(address)) {
			builder.addPropertyValue("address", address);
		}
		if (StringUtils.hasText(name)) {
			builder.addPropertyValue("name", name);
		}

		if (StringUtils.hasText(protocol)) {
			builder.addPropertyValue("protocol", protocol);
		}

		if (StringUtils.hasText(basePackage)) {
			builder.addPropertyValue("basePackage", basePackage);
		}

		if (StringUtils.hasText(timeout)) {
			builder.addPropertyValue("timeout", timeout);
		}

		if (StringUtils.hasText(port)) {
			builder.addPropertyValue("port", port);
		}
		System.out.println("id :" + id +" name : " +name +"  address ：" +address);

		//扫描包
		List<Object> list = new ArrayList<>();
		Set<Class<?>> sets = PkgUtil.getClzFromPkg("com.zzq.provider");
		sets.forEach((k)->{
			if (-1 != k.getSimpleName().indexOf("Impl") && !"RegistryCenterImpl".equals(k.getSimpleName())) {
				System.out.println("class " + k);
				//实例化对象
				Object obj = BeanUtils.instantiateClass(k);
				list.add(obj);
			}
		});
		Object[] objects = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			objects[i] = list.get(i);
		}
		IRegistryCenter registryCenter = new RegistryCenterImpl(address);
		//服务发布,监听端口
		RpcServer rpcServer = new RpcServer(registryCenter, getIntranetIp()+":"+port);

		//一个服务名称可能对应多个不同实现
		//服务名称和实例对象的关系
		rpcServer.bind(objects);

		rpcServer.publisher();
	}
	/**
	 * 获取内网ip
	 */
	public static String getIntranetIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return null;
		}
	}

}
