package com.ouliang.register;

import com.ouliang.common.URL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 模拟远程注册中心
public class MapRemoteRegister {
    private static Map<String, List<URL>> map = new HashMap<>();

    static {

    }

    private static void cleanUp() {
        for (String key : map.keySet()) {
            map.get(key).removeIf(URL::isExpired);
        }
        save(false);
    }

    public static void registerOnTime(String interfaceName, String version, URL url) {
        new Thread((Runnable) () -> {
            while (true) {
                System.out.println("线程启动");
                List<URL> list = map.get(interfaceName + version);
                if (list == null) {
                    list = new ArrayList<>();
                }
                url.setExpiryTime();
                list.add(url);
                map.put(interfaceName + version, list);
                save(true);
                try {
                    Thread.sleep(90000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("方法完毕------------------");

    }

    public static void register(String interfaceName, String version, URL url) {
        map = get();
        List<URL> list = map.get(interfaceName + version);
        if (list == null) {
            list = new ArrayList<>();
        }
        url.setExpiryTime();
        list.add(url);
        map.put(interfaceName + version, list);
        save(true);
    }

    public static List<URL> get(String interfaceName) {
        map = get();
        cleanUp();
        return map.get(interfaceName);
    }

    private static void save(boolean flag) {
//        if (new File("register.txt").exists() && flag) {
////            map.putAll(get());// 键值对替换了
//            Map<String, List<URL>> c = get();
//             c.putAll(map);
//             map=c;
//        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("register.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
            oos.writeObject(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Map<String, List<URL>> get() {
        if (!new File("register.txt").exists()) return new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("register.txt");
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            return (Map<String, List<URL>>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
