package com.ouliang;

import com.ouliang.protocol.HttpServer;
import com.ouliang.register.LocalRegister;

public class Provider {
    public static void main(String[] args) {

        // 服务注册
        LocalRegister.register(TestService.class.getName(),"1.0",TestServiceImpl.class);
        LocalRegister.register(TestService.class.getName(),"2.0",TestServiceImpl1.class);

        // Netty , Tomcat
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost",8899);
    }
}
