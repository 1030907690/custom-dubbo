package com.zzq.provider;

import com.zzq.provider.api.ITestService;
import com.zzq.provider.customtag.pojo.Dubbo;
import com.zzq.provider.registry.IRegistryCenter;
import com.zzq.provider.registry.RegistryCenterImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: 提供者启动 - 第二版改动
 * @date 2019/2/11 17:35
 */
public class StartProvider {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-base.xml");

        String[] beanNames = context.getBeanDefinitionNames();

        for (String beanName : beanNames) {
            System.out.println("beanName: " + beanName);
        }

        Dubbo dubbo = context.getBean(Dubbo.class);

        try {
            String basePath = StartProvider.class.getResource("/").getPath();
            System.out.println("basePath = [" + basePath + "]");

            //扫描路径
            String packageSearchPath = basePath + resolveBasePackage(dubbo.getBasePackage());
            System.out.println("扫描路径 packageSearchPath = [" + packageSearchPath + "]");
            File fileFolder = new File(packageSearchPath);
            File[] listFiles = fileFolder.listFiles();


            List<Object> list = new ArrayList<>();
            for (File file : listFiles) {
                if (-1 != file.getAbsolutePath().indexOf("Impl")) {
                    System.out.println("file = [" + file + "]" + " fileName : " + file.getName());
                    //实例化对象
                    Object obj = BeanUtils.instantiateClass(Class.forName(dubbo.getBasePackage() + "." + file.getName().replace(".class", "")));
                    list.add(obj);
                }
            }

            Object[] objects = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                objects[i] = list.get(i);
            }


            IRegistryCenter registryCenter = new RegistryCenterImpl(dubbo.getAddress());

            //服务发布,监听端口
            RpcServer rpcServer = new RpcServer(registryCenter, "127.0.0.1:"+dubbo.getPort());

            //一个服务名称可能对应多个不同实现
            //服务名称和实例对象的关系
            rpcServer.bind(objects);

            rpcServer.publisher();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(basePackage);
    }

}
