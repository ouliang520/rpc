package com.ouliang;

import com.ouliang.registerInterface.Register;

@Register(version = "1.0", hostname = "localhost", port = 8899)
public class TestServiceImpl implements TestService {

    @Override
    public String tryTest(String name) {
        return "hello: " + name;
    }
}
