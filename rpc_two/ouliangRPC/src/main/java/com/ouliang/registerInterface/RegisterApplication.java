package com.ouliang.registerInterface;

import com.ouliang.common.URL;
import com.ouliang.protocol.HttpServer;
import com.ouliang.register.LocalRegister;
import com.ouliang.register.MapRemoteRegister;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class RegisterApplication {
    public static void run(String args) {
        // 获取当前类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 获取所有类的定义
        try {
            List<Class<?>> classes = getClasses(args); // 替换为你的包名

            for (Class<?> serviceClass : classes) {
                // 检查是否带有@MyRegister注解
                if (serviceClass.isAnnotationPresent(Register.class)) {
                    Register annotation = serviceClass.getAnnotation(Register.class);
                    String version = annotation.version();
                    String hostname = annotation.hostname();
                    int port = annotation.port();
                    String interfaceName = serviceClass.getInterfaces()[0].getName();

                    // 服务注册
                    LocalRegister.register(interfaceName, version, serviceClass);

                    // 注册中心注册
                    URL url = new URL(hostname, port);
                    MapRemoteRegister.register(interfaceName, version, url);

                    // Netty , Tomcat
                    HttpServer httpServer = new HttpServer();
                    httpServer.start(url.getHostname(), url.getPort());
//                    System.out.println(version);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<java.net.URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            java.net.URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile() && file.getName().endsWith(".class")) {
                                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                                Class<?> clazz = classLoader.loadClass(className);
                                classes.add(clazz);
                            }
                        }
                    }
                }
            }
        }
        return classes;
    }
}

