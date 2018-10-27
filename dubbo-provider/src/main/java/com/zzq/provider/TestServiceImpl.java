package com.zzq.provider;

import com.zzq.provider.annotation.RpcAnnotation;
import com.zzq.provider.api.ITestService;

@RpcAnnotation(value = ITestService.class)
public class TestServiceImpl implements ITestService {

    @Override
    public String test(String name) {
        System.out.println("test method " + name);
        return null;
    }
}
