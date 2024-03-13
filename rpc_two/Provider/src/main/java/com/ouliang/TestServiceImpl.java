package com.ouliang;

import com.ouliang.pojo.Student;

public class TestServiceImpl implements TestService{

    @Override
    public String tryTest(String name) {
        return "hello: " + name;
    }

    @Override
    public Student selectStudent(int id, String name) {
        return new Student(id,name);
    }
}
