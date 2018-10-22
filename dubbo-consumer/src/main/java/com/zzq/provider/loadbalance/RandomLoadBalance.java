package com.zzq.provider.loadbalance;

import java.util.List;
import java.util.Random;


/***
 * 随机策略
 * */
public class RandomLoadBalance implements ILoadBalance {

    @Override
    public String select(List<String> list) {

        Random random = new Random();
        int size = null == list ? 0 : list.size();
        //随机拿一个节点
        return (size > 0) ? list.get(random.nextInt(size)) : null;
    }
}
