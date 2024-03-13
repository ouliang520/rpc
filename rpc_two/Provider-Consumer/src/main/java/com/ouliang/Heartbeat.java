package com.ouliang;

// 心跳机制,服务提供者必须实现的心跳方法
public interface Heartbeat {
    default boolean heart(){
        System.out.println("心跳发送");
        return true;
    }
}
