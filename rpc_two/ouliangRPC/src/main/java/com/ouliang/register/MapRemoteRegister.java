package com.ouliang.register;

import com.ouliang.common.URL;
import com.ouliang.utils.TestUtils;

import java.io.*;
import java.util.*;

// 模拟远程注册中心
public class MapRemoteRegister {
    private static final String filePath = "rpc_two/ouliangRPC/src/main/resources/register.properties";
    private static Map<String, List<URL>> map = new HashMap<>();

    public static void register(String interfaceName, URL url) {
        List<URL> list = map.get(interfaceName);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(url);
        map.put(interfaceName, list);

        save();
    }

    public static List<URL> get(String interfaceName) {
        map = get();
        return map.get(interfaceName);
    }

    private static void save() {
        try {
            // 将Map中的数据写入Properties对象
            for (Map.Entry<String, List<URL>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<URL> urls = entry.getValue();
                StringBuilder urlsString = new StringBuilder();
                for (URL url : urls) {
                    urlsString.append(url.getHostname()).append(":").append(url.getPort()).append(",");
                }

                TestUtils.addProps(new File(filePath), key, urlsString.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Map<String, List<URL>> get() {
        Properties properties = new Properties();
        Map<String, List<URL>> map = new HashMap<>();

        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);

            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                List<URL> urls = new ArrayList<>();
                for (String urlString : value.split(",")) {
                    String[] parts = urlString.split(":");
                    if (parts.length == 2) {
                        String hostname = parts[0];
                        Integer port = Integer.parseInt(parts[1]);
                        URL url = new URL(hostname, port);
                        urls.add(url);
                    } else {
                        System.out.println("Invalid URL format: " + urlString);
                    }
                }
                map.put(key, urls);
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
