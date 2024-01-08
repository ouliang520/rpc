package com.ouliang;

import com.ouliang.common.Invocation;
import com.ouliang.protocol.HttpClient;

public class Consumer {

    public static void main(String[] args) {
//        TestService service = null;
//        String result = service.tryTest("ouliang");
//        System.out.println(result);

        Invocation invocation = new Invocation(TestService.class.getName(), "tryTest", new Class[]{String.class}, new Object[]{"ouliang"}, "1.0");
        HttpClient httpClient = new HttpClient();
        String s = (String) httpClient.send("localhost",8899,invocation);
        System.out.println(s);



    }
}
