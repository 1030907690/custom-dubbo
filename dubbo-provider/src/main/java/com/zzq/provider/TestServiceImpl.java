package com.zzq.provider;

import com.zzq.provider.annotation.RpcAnnotation;

@RpcAnnotation(value = ITestService.class)
public class TestServiceImpl implements ITestService {

    @Override
    public String test(String name) {
        System.out.println("test method ");
        return null;
    }
}
