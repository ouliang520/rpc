package com.ouliang;

import com.ouliang.common.Invocation;
import com.ouliang.protocol.HttpClient;
import com.ouliang.proxy.ProxyFactory;

public class Consumer {

    public static void main(String[] args) {


        // 被封装的细节
        /**
         Invocation invocation = new Invocation(TestService.class.getName(), "tryTest", new Class[]{String.class}, new Object[]{"ouliang"}, "1.0");
         HttpClient httpClient = new HttpClient();
         String s = (String) httpClient.send("localhost",8899,invocation);
         **/

        // 代理封装细节
        TestService service = ProxyFactory.getProxy(TestService.class,"1.0");
        String result = service.tryTest("ouliang");
        System.out.println(result);
    }
}
