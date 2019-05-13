package com.zzq.test.lock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: zookeeper分布式锁
 * @date 2019/5/13 10:58
 */
public class ZookeeperDistributedLock implements Lock,Watcher{


    private  final String LOCK_NODE = "/LOCK";

    private 

    public ZookeeperDistributedLock(){

    }

    @Override
    public void lock() {

    }


    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
