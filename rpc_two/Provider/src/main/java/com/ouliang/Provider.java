package com.ouliang;

import com.ouliang.common.URL;
import com.ouliang.protocol.HttpServer;
import com.ouliang.register.LocalRegister;
import com.ouliang.register.MapRemoteRegister;

public class Provider {
    public static void main(String[] args) {

        // 服务注册
        LocalRegister.register(TestService.class.getName(),"1.0",TestServiceImpl.class);
        LocalRegister.register(TestService.class.getName(),"2.0",TestServiceImpl1.class);

        // 注册中心注册
        URL url = new URL("localhost",8899);
        MapRemoteRegister.register(TestService.class.getName(),url);

        // Netty , Tomcat
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(),url.getPort());
    }
}
