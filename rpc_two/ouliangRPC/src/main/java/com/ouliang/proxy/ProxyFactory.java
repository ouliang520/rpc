package com.ouliang.proxy;

import com.ouliang.common.Invocation;
import com.ouliang.common.URL;
import com.ouliang.loadbalance.Loadbalancer;
import com.ouliang.protocol.HttpClient;
import com.ouliang.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class ProxyFactory {
    public static <T> T getProxy(Class interfaceClass,String version) {
        //用户配置

        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(), method.getParameterTypes(), args, version);
                HttpClient httpClient = new HttpClient();

                // 服务发现
                List<URL> list = MapRemoteRegister.get(interfaceClass.getName()+version);

                // 负载均衡
                URL url = Loadbalancer.random(list);

                //服务调用
                Object s = httpClient.send(url.getHostname(), url.getPort(), invocation);
                return s;
            }
        });
        return (T) proxyInstance;
    }
}
