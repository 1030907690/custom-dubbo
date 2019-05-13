package com.zzq.test.lock;



import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ZookeeperDistributedLock implements Lock {

    private Logger log = LoggerFactory.getLogger(this.getClass()); // 日志对象


    private final String LOCK_NODE = "/LOCK";

    private ZkClient client = new ZkClient("localhost:2181");

    /*** 全局变量 测试到并发多了cdl会阻塞 局部变量没问题*/
    // private CountDownLatch cdl;

    public ZookeeperDistributedLock() {

    }

    @Override
    public void lock() {
        if (tryLock()) {
            return;
        }
        waitForLock();
        lock();
    }

    /***
     * 等待释放
     * */
    private void waitForLock() {

        final CountDownLatch cdl = new CountDownLatch(1);
        //给节点加监听
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                //数据改变
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                log.info("数据删除事件");
                //数据被删除
                if(null != cdl){
                    //释放
                    cdl.countDown();
                }
            }
        };

        client.subscribeDataChanges(LOCK_NODE,listener);
        //目前想到3种办法阻塞  1、循环判断节点释放存在  2、使用CountDownLatch 3、睡眠一会儿
        //1. while(client.exists(LOCK_NODE)){}
        //2.
        if(client.exists(LOCK_NODE)){
            try {
                //cdl = new CountDownLatch(1);
                //阻塞
                cdl.await();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        /* 3.
        try {
            Thread.sleep(10);
        }catch (Exception e){
            e.printStackTrace();
        }*/

        client.unsubscribeDataChanges(LOCK_NODE,listener);

    }


    @Override
    public boolean tryLock() {
        try {
            client.createPersistent(LOCK_NODE);
            //加锁成功
            return true;
        } catch (ZkNodeExistsException e) {
            //加锁失败
            return false;
        }
    }

    @Override
    public void unlock() {
        client.delete(LOCK_NODE);
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


}
