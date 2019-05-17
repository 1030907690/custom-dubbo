package com.zzq.test.lock;

import com.alibaba.fastjson.JSON;
import org.springframework.core.annotation.Order;
import sun.net.www.http.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: zookeeper分布式锁测试
 * @date 2019/5/13 16:26
 */
public class ZookeeperLockTest {

    private static int threadCount = 100;


    //static Lock lock = new ZookeeperDistributedLock();
    static Lock lock = new ZookeeperImproveLock();
    private static CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(threadCount); //为保证threadCount个线程同时并发运行

    //等待线程全部执行完
    private static CountDownLatch COUNT_DOWN_LATCH_WAIT = new CountDownLatch(threadCount);

    static Map<String, Order> map = new HashMap<>();

    public static void main(String[] args) {
        Order order = new Order();
        order.setOrderId("1");
        order.setStatus(0);
        map.put(order.getOrderId(), order);
        main();

    }

    public static void main() {

        AtomicInteger count = new AtomicInteger(0);
         /* 测试并发问题*/
        for (int i = 0; i < threadCount; i++) {//循环开threadCount个线程

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        COUNT_DOWN_LATCH.countDown();//每次减一
                        COUNT_DOWN_LATCH.await(); //此处等待状态，为了让30个线程同时进行
                        lock.lock();
                        try {
                            //得到订单
                            Order order = map.getOrDefault("1", new Order());
                            if (order.getStatus().equals(0)) {
                                System.out.println("订单未支付,修改状态");
                                //修改为支付状态
                                order.setStatus(1);
                                System.out.println("order :" + JSON.toJSONString(order));
                                count.incrementAndGet();
                            } else {
                                System.out.println("订单已支付");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            lock.unlock();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //每执行完一个减一
                        COUNT_DOWN_LATCH_WAIT.countDown();
                    }

                }
            }).start();


        }
        try {
            //阻塞主线程
            COUNT_DOWN_LATCH_WAIT.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("修改状态被执行了 [" + count.get() + "]次");

    }

    public static class Order {
        //订单id
        String orderId;
        //状态  0 未支付  1 已支付
        Integer status;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}
