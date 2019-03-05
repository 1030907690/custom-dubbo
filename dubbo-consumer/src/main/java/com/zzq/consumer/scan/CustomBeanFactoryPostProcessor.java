package com.zzq.consumer.scan;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	int i = 0;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

		if(i < 1 ){
			ClassPathMapperScanner scanner = new ClassPathMapperScanner((BeanDefinitionRegistry) configurableListableBeanFactory);
			System.out.println("scanner = [" + scanner + "]");
			scanner.scan(StringUtils.tokenizeToStringArray("com.zzq.provider.api.*", ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
		}
		i++;

	}
}
