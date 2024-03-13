package com.ouliang;

import com.ouliang.pojo.Student;

public class TestServiceImpl1 implements TestService,Heartbeat{

    @Override
    public String tryTest(String name) {
        return "bye: " + name;
    }

    @Override
    public Student selectStudent(int id, String name) {
        return null;
    }
}
