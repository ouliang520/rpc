package com.ouliang.utils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ...
 *
 * @author yaoshiquan
 * @date 2023/9/1
 */
public class TestUtils {

    /**
     * 修改配置文件
     *
     * @param propsFile 配置文件
     * @param key       键
     * @param value     值
     */
    public static void setProps(File propsFile, String key, String value) throws IOException {
        consumeProps(propsFile, props -> props.setProperty(key, value));
    }

    /**
     * 新增配置文件
     *
     * @param propsFile 配置文件
     * @param key       键
     * @param value     值
     */
    public static void addProps(File propsFile, String key, String value) throws IOException {
        setProps(propsFile, key, value);
    }

    /**
     * 删除配置文件
     *
     * @param propsFile 配置文件
     * @param keys      键
     */
    public static void removeProps(File propsFile, String... keys) throws IOException {
        consumeProps(propsFile, props -> Arrays.stream(keys).forEach(props::remove));
    }

    public static void consumeProps(File propsFile, Consumer<Properties> consumer) throws IOException {
        Properties properties = new Properties();
        String parentFile = propsFile.getParent();
        // 使用UUID拼接临时文件名
        File tempFile = new File(parentFile, UUID.randomUUID() + ".tmp");
        // 复制
        Files.copy(propsFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(tempFile.toPath()), StandardCharsets.UTF_8))) {
            properties.load(reader);
        }
        consumer.accept(properties);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(tempFile.toPath()), StandardCharsets.UTF_8))) {
            properties.store(writer, null);
        }
        // 替换原始文件
        Files.move(tempFile.toPath(), propsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 递归检查目录下是否存在文件
     *
     * @param dir            文件夹
     * @param targetFileName 目标文件名
     * @return 是否存在
     */
    public static boolean existFile(File dir, String targetFileName) {
        return functionDir(dir, false, false, file -> file.getName().equals(targetFileName), (prev, curr) -> prev || curr);
    }

    /**
     * 删除文件
     *
     * @param file   文件
     * @param delDir 是否删除目录
     * @return 是否删除成功
     */
    public static boolean deleteFile(File file, boolean delDir) {
        return functionDir(file, delDir, true, File::delete, (prev, curr) -> prev && curr);
    }

    /**
     * 递归消费文件和目录
     *
     * @param file        文件
     * @param functionDir 回溯过程是否消费目录
     * @param init        初始布尔值，用于最后结果的逻辑运算
     * @param function    用于消费文件和目录的函数式接口
     * @param reducer     函数式接口用于计算执行结果
     * @return 执行结果
     */
    public static boolean functionDir(File file, boolean functionDir, boolean init, Function<File, Boolean> function, BinaryOperator<Boolean> reducer) {
        // 获取目录下子文件
        File[] files = file.listFiles();
        // 遍历该目录下的文件对象
        boolean res = init;
        if (Objects.nonNull(files)) {
            for (File f : files) {
                // 判断子目录是否存在子目录，如果是目录，则递归消费
                res = reducer.apply(res, functionFile(f, functionDir, init, function, reducer));
            }
        }
        // 消费完目录的所有子孙文件目录后，最后消费目录
        return reducer.apply(res, functionDir ? function.apply(file) : init);
    }

    /**
     * 消费文件夹函数式接口
     */
    private static boolean functionFile(File file, boolean consumeDir, boolean init, Function<File, Boolean> function, BinaryOperator<Boolean> reducer) {
        if (file.isDirectory()) {
            return functionDir(file, consumeDir, init, function, reducer);
        } else {
            // 消费文件
            return function.apply(file);
        }
    }

    /**
     * 模拟修改
     *
     * @param file 被修改文件
     * @param str  添加字符
     */
    public static void appendStr2File(File file, String str) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(file, true))) {
            printWriter.print(str);
        }
    }

    public static File getTestClassDir(URL resource) {
        return new File(URI.create(resource.toString()));
    }

}
