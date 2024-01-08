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
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("/register.txt");
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
