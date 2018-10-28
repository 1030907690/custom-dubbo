package com.zzq.provider;

import com.zzq.provider.annotation.RpcAnnotation;
import com.zzq.provider.api.ITestService;

@RpcAnnotation(value = ITestService.class)
public class TestServiceImpl implements ITestService {

    @Override
    public String test(String name) {
        String result = "test method " + name;
        System.out.println(result);
        return result;
    }
}
