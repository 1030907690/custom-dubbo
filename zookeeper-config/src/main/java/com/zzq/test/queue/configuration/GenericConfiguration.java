package com.zzq.test.queue.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: 一些的配置
 * @date 2019/4/16 17:48
 */
@Configuration
public class GenericConfiguration {


    private String zookeeperAddress;

    @Bean
    public ExecutorService executorService(){
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = new ThreadPoolExecutor(processors * 2, processors * 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(processors * 100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        return executorService;
    }

    public String getZookeeperAddress() {
        if(null == this.zookeeperAddress){
            this.zookeeperAddress = "127.0.0.1:2181";
        }
        return zookeeperAddress;
    }

    public void setZookeeperAddress(String zookeeperAddress) {
        this.zookeeperAddress = zookeeperAddress;
    }
}