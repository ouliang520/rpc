package com.ouliang;

import com.ouliang.protocol.HttpServer;

public class Provider {
    public static void main(String[] args) {

        // Netty , Tomcat
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost",8899);
    }
}
