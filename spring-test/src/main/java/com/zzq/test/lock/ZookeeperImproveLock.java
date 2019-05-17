package com.zzq.test.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/5/17 16:20
 */
public class ZookeeperImproveLock implements Lock {

    private Logger log = LoggerFactory.getLogger(this.getClass()); // 日志对象


    private final String LOCK_NODE = "/LOCK";

    private ZkClient client = new ZkClient("localhost:2181", 1000, 1000, new SerializableSerializer());

    String beforePath;

    String currentPath;

    private CountDownLatch cdl;

    public ZookeeperImproveLock() {
        if (!client.exists(LOCK_NODE)) {
            client.createPersistent(LOCK_NODE);
        }
    }


    @Override
    public void lock() {
        if (!tryLock()) {
            waitForLock();
            lock();
        } else {
            log.info(Thread.currentThread() + "获得锁");
        }
    }

    /***
     * 等待释放
     * */
    private void waitForLock() {
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                log.info("数据删除事件");
                if (null != cdl) {
                    cdl.countDown();
                }
            }
        };

        client.subscribeDataChanges(beforePath, listener);

        if (client.exists(LOCK_NODE)) {
            cdl = new CountDownLatch(1);
            try {
                //阻塞
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        client.unsubscribeDataChanges(beforePath, listener);
    }


    @Override
    public boolean tryLock() {

        if (currentPath == null || currentPath.length() <= 0) {
            currentPath = this.client.createEphemeralSequential(LOCK_NODE + "/", "lock");
            System.out.println("----------------- " + currentPath);
        }

        List<String> childrens = client.getChildren(LOCK_NODE);
        Collections.sort(childrens);

        System.out.println("currentPath :" + currentPath + " LOCK_NODE + \"/\" + childrens.get(0))" + LOCK_NODE + "/" + childrens.get(0));
        ;
        if (currentPath.equals(LOCK_NODE + "/" + childrens.get(0))) {
            return true;
        } else {
            int wz = Collections.binarySearch(childrens, currentPath.substring(6));
            beforePath = LOCK_NODE + "/" + childrens.get(wz - 1);
        }
        return false;
    }

    @Override
    public void unlock() {
        client.delete(currentPath);
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
