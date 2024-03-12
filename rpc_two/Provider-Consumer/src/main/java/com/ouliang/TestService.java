package com.ouliang;

import com.ouliang.pojo.Student;

public interface TestService {
    public String tryTest(String name);

    public Student selectStudent(int id,String name);
}
