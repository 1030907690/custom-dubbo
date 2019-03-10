package com.zzq.consumer.configuration;

import com.zzq.consumer.bean.MapperBean;
import com.zzq.consumer.registry.IServiceDiscovery;
import com.zzq.consumer.registry.ServiceDiscoveryImpl;
import com.zzq.consumer.util.PkgUtil;
import com.zzq.provider.api.ITestService;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * 注入service
 * **/
public class ServiceScannerConfigurer implements ImportBeanDefinitionRegistrar {

	private IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl("127.0.0.1:2181");

	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {

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

			//将testService注册进入spring容器 ,其实真正调用时是FactoryBean里面的代理对象
			registry.registerBeanDefinition(beanName, beanDefinition );
		});


		/*GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(MapperBean.class);
		MutablePropertyValues values = beanDefinition.getPropertyValues();
		values.addPropertyValue("serviceDiscovery",serviceDiscovery);
		values.addPropertyValue("mapperInterface", ITestService.class);
		//将testService注册进入spring容器 ,其实真正调用时是FactoryBean里面的代理对象
		registry.registerBeanDefinition("testService", beanDefinition );*/
	}




}
