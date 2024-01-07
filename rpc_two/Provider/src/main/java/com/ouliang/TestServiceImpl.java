package com.ouliang;

public class TestServiceImpl implements TestService{

    @Override
    public String tryTest(String name) {
        return "hello: " + name;
    }
}
