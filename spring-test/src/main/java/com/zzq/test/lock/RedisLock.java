/*
package com.zzq.test.lock;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

*/
/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: redis锁
 * @date 2019/2/17 15:14
 *//*

@Component
public class RedisLock implements Lock {

    private static final String KEY = "REDIS_LOCK:GAIN_MONEY";



    private static final String PX = "PX";

    private static final String NX = "NX";


    private static final String OK = "OK";

    private static final int time = 4000;

    @Resource
    private JedisConnectionFactory jedisConnectionFactory;

    private ThreadLocal<String> local = new ThreadLocal<>();



    @Override
    public void lock() {
        //尝试加锁
        if (tryLock()) {
            return;
        }
        //如果加锁失败，当前线程先休眠一下
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //递归再尝试
        lock();
    }


    //使用setNX命令返回OK加锁成功,并产生随机值
    @Override
    public boolean tryLock() {
        //每个尝试获得锁的线程产生一个随机值,便于释放对应的锁
        String uuid = UUID.randomUUID().toString();
        //获取jedis对象
        Jedis jedis = (Jedis)jedisConnectionFactory.getConnection().getNativeConnection();
        //使用setNX命令请求写值，并设置失效时间
        String ret = jedis.set(KEY,uuid,NX,PX,time);
        //System.out.println("ret "  + ret);
        jedis.close();
        //返回OK则获得锁成功
        if(OK.equals(ret)){
            //保存uuid
            local.set(uuid);
            return true;
        }
        //返回获得锁失败
        return false;
    }

    //解锁
    @Override
    public void unlock() {
        //删除数据lua脚本
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //jedis连接
        Jedis jedis = (Jedis)jedisConnectionFactory.getConnection().getNativeConnection();
        //执行脚本
        jedis.eval(luaScript, Arrays.asList(KEY),Arrays.asList(local.get()));

        jedis.close();
    }


    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
}
*/
