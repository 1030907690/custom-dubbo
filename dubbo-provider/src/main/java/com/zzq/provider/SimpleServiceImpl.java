package com.zzq.provider;

import com.zzq.provider.api.ISimpleService;

public class SimpleServiceImpl implements ISimpleService {

    @Override
    public String simpleTest(String name) {
        System.out.print("simpleTest ");
        return null;
    }
}
