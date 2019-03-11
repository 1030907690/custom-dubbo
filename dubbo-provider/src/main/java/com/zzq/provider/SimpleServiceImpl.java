package com.zzq.provider;

import com.zzq.provider.annotation.RpcAnnotation;
import com.zzq.provider.api.ISimpleService;
import com.zzq.provider.api.ITestService;

@RpcAnnotation(value = ISimpleService.class)
public class SimpleServiceImpl implements ISimpleService {

    @Override
    public String simpleTest(String name) {
        System.out.print("simpleTest " + name);
        return "simpleTest  + name";
    }
}
