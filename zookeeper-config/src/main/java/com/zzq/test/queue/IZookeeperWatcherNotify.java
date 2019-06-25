package com.zzq.test.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: zookeeper watcher监听处理
 * @date 2019/6/18 16:15
 */
public interface IZookeeperWatcherNotify {

    /***
     * zhouzhongqing
     * 2019年6月18日16:19:04
     * 处理zookeeper的watcher监听
     * */
    void process(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent);

    /***
     * 判断当前这个处理类是否支持
     * 支持 返回true 不支持 返回false 则不在调用process
     * @param curatorFramework
     * @param treeCacheEvent
     * */
    boolean supports(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent);
}
