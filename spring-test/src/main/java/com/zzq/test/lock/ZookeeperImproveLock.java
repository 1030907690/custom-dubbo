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


    public ZookeeperImproveLock() {
        if (!client.exists(LOCK_NODE)) {
            client.createPersistent(LOCK_NODE);
        }
    }


    @Override
    public void lock() {
        tryLock();

    }

    /***
     * 等待释放
     *  把前一个加入监听,等待前一个释放
     * */
    private void waitForLock(String beforePath) {
        final CountDownLatch cdl = new CountDownLatch(1);
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                // System.out.println("数据删除事件");
                if (null != cdl) {
                    cdl.countDown();
                }
            }
        };

        client.subscribeDataChanges(beforePath, listener);

        if (client.exists(LOCK_NODE)) {
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


        String currentPath = this.client.createEphemeralSequential(LOCK_NODE + "/", "lock");


        List<String> childrens = client.getChildren(LOCK_NODE);
        Collections.sort(childrens);

        if (currentPath.equals(LOCK_NODE + "/" + childrens.get(0))) {
            return true;
        } else {
            int wz = Collections.binarySearch(childrens, currentPath.substring(6));
            String beforePath = LOCK_NODE + "/" + childrens.get(wz - 1);
            //System.out.println("beforePath "+ beforePath);
            //把前一个加入监听,等待前一个释放
            waitForLock(beforePath);
        }
        return false;
    }

    @Override
    public void unlock() {

        List<String> childrens = client.getChildren(LOCK_NODE);
        Collections.sort(childrens);
        //System.out.println("数据删除 "+LOCK_NODE +  "/" + childrens.get(0));
        client.delete(LOCK_NODE + "/" + childrens.get(0));
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
