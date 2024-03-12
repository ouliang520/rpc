package com.ouliang.loadbalance;

import com.ouliang.common.URL;

import java.util.List;
import java.util.Random;
//负载均衡
public class Loadbalancer {
    public static URL random(List<URL> urls){
        Random random = new Random();
        int i= random.nextInt(urls.size());
        return urls.get(i);
    }
}
