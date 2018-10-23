package com.zzq.consumer;

public class TestServiceImpl implements ITestService {
    @Override
    public String test(String name) {
        System.out.println("test method ");
        return null;
    }
}
