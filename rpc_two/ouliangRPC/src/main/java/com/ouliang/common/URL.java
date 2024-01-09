package com.ouliang.common;

import java.io.Serializable;

public class URL implements Serializable {

    private String hostname;
    private Integer port;
    long expiryTime;

    public URL(String hostname, Integer port) {
        this.hostname = hostname;
        this.port = port;
        this.expiryTime = System.currentTimeMillis();
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    // 更新过期时间
    public void setExpiryTime() {
        this.expiryTime += 10000;
    }

    public boolean isExpired() {
        return false;
//        return System.currentTimeMillis() > getExpiryTime();
    }
}
