package com.zzq.test.zookeeper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/5/8 15:02
 */

@Configuration
public class GenericConfig {

    @Bean
    public ConfigureContainer configureContainer() {
        return new ConfigureContainer();
    }

    @Bean
    public ExecutorService executorService() {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = new ThreadPoolExecutor(processors * 2, processors * 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(processors * 100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        return executorService;
    }
}
