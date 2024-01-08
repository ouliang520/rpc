package com.ouliang;

public class TestServiceImpl1 implements TestService{

    @Override
    public String tryTest(String name) {
        return "bye: " + name;
    }
}
