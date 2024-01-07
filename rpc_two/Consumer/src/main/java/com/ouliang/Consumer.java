package com.ouliang;

public class Consumer {

    public static void main(String[] args) {
        TestService service = null;
        String result = service.tryTest("ouliang");
        System.out.println(result);
    }
}
