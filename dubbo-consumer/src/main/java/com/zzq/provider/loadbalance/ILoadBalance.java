package com.zzq.provider.loadbalance;

import java.util.List;

/**
 * 负载均衡
 * **/
public interface ILoadBalance {

    String select(List<String> list);
}
