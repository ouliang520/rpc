package com.ouliang.protocol;

import com.ouliang.common.Invocation;
import com.ouliang.register.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServerHandler {

    public void handler(ServletRequest req, ServletResponse resp) {
        // 处理请求-->接口,方法,方法参数
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();
            String interfaceName = invocation.getInterfaceName();
            System.out.println(interfaceName);
            Class classImpl = LocalRegister.get(interfaceName,invocation.getVersion());
            Method method = classImpl.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            Object result = method.invoke(classImpl.newInstance(), invocation.getParameters());

            IOUtils.write((String) result,resp.getOutputStream()); //
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
