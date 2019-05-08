package com.zzq.test.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.ExecutorService;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: 启动zookeeper配置中心服务
 * @date 2019/5/8 14:43
 */
public class StartZookeeperConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GenericConfig.class);

        ConfigureContainer configureContainer = context.getBean(ConfigureContainer.class);
        ExecutorService executorService = context.getBean(ExecutorService.class);

        //启动
        executorService.execute(() -> {
            try {
                ZookeeperClientUpgrade zk = new ZookeeperClientUpgrade(configureContainer);
                zk.initialize();
                zk.create("/testRootPath", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.EPHEMERAL);
                //zk.getData("/testRootPath");
                // zk.deletNode("/testRootPath",-1);
                System.in.read();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
