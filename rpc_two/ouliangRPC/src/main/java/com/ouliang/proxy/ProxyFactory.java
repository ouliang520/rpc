package com.ouliang.proxy;

import com.ouliang.common.Invocation;
import com.ouliang.protocol.HttpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {
    public static <T> T getProxy(Class interfaceClass,String version) {
        //用户配置

        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(), method.getParameterTypes(), args, version);
                HttpClient httpClient = new HttpClient();
                String s = (String) httpClient.send("localhost",8899,invocation);
                return s;
            }
        });
        return (T) proxyInstance;
    }
}
