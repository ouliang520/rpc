package com.ouliang.register;

import com.ouliang.common.URL;
import com.ouliang.utils.TestUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

// 模拟远程注册中心
public class MapRemoteRegister {
    private static final String filePath = "rpc_two/ouliangRPC/src/main/resources/register.properties";
    private static Map<String, List<URL>> map = new HashMap<>();
//    private static FileEventMonitor fileEventMonitor;

    public static void register(String interfaceName, String version, URL url) {
//        监听器与注册中心应该独立于提供者,为了方便将注册接口直接提供
//        if (fileEventMonitor == null) {
//            try {
//                fileEventMonitor = new FileEventMonitor(500L);
//                fileEventMonitor.startWatch(new FileEventMonitor.FileEventCallback() {
//                    @Override
//                    public void OnHeart(File file) {
//
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        List<URL> list = map.get(interfaceName + version);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(url);
        map.put(interfaceName + version, list);

        // 加入监听
//        fileEventMonitor.watch(interfaceName, version, url);
        watch(interfaceName, version, url);
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

    /**
     * 通知注册中心
     */

    public static void watch(String interfaceName, String version, URL url){
        try {
            Socket s = new Socket("localhost", 8888);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(interfaceName);
            dos.writeUTF(version);
            dos.writeUTF(url.getHostname());
            dos.writeInt(url.getPort());

            s.getOutputStream().write(baos.toByteArray());
            s.getOutputStream().flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
