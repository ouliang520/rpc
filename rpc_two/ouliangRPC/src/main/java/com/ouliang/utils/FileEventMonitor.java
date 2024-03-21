package com.ouliang.utils;

import com.ouliang.common.Invocation;
import com.ouliang.common.URL;
import com.ouliang.protocol.HttpClient;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;

public class FileEventMonitor<T> {
    private static final String filePath = "rpc_two/ouliangRPC/src/main/resources/register.properties";
    private final Queue<WatchContext> watchContextQueue = new ConcurrentLinkedQueue<>();

    /**
     * 定时轮询线程，用于扫描队列处理事件回调
     */
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1,
            runnable -> {
                Thread thread = Executors.defaultThreadFactory().newThread(runnable);
                thread.setName(this.getClass().getSimpleName()); // 设置扫描线程名称
                thread.setDaemon(true); // 设置守护线程
                return thread;
            });

    /**
     * 定时轮询线程的轮询间隔，单位为ms
     */
    private final Long watchInterval;

    /**
     * 定时轮询线程的扫描任务
     */
    private ScheduledFuture<?> future;

    public FileEventMonitor(Long watchInterval) throws IOException {
        this.watchInterval = watchInterval;
    }

    /**
     * 开始监听
     *
     * @param callback 关注的事件发生时执行的回调
     */
    public void startWatch(FileEventCallback<T> callback) throws IOException {
        if (Objects.isNull(future)) {
            future = scheduledExecutor.scheduleWithFixedDelay(new WatchTask(callback), 1000, watchInterval, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 停止监听
     */
    public void stopWatch() throws IOException {
        if (Objects.isNull(future)) {
            return;
        }

        if (Objects.nonNull(future)) {
            future.cancel(false); // false表示如果扫描线程在处理，则等它处理完
            future = null;
        }

    }

    /**
     * 注册监听
     */
    public void watch(String interfaceName, String version, URL url) {
        watchContextQueue.add(new WatchContext(interfaceName, version, url));
    }

    /**
     * 取消监听
     */
    private void unWatch(String interfaceNameAndVersion, URL url) {
        for (WatchContext watchContext : watchContextQueue) {
            if (watchContext.getInterfaceName() + watchContext.getVersion() == interfaceNameAndVersion) {
                watchContextQueue.remove(watchContext);
            }
        }
    }

    private void unWatch(WatchContext context) {
        watchContextQueue.remove(context);
        String value = null;
        try {
            value = TestUtils.getPros(new File(filePath), context.getInterfaceName() + context.getVersion());
            URL url = context.getUrl();
            String removeUrl = url.getHostname() + ":" + url.getPort() + ",";
            value = value.replace(removeUrl, "");

            if ("".equals(value)) {
                TestUtils.removeProps(new File(filePath), context.getInterfaceName() + context.getVersion());
            } else {
                TestUtils.setProps(new File(filePath), context.getInterfaceName() + context.getVersion(), value);
            }
        } catch (IOException e) {
            System.err.println("找不到配置文件");
        }
    }

    public class WatchTask implements Runnable {
        private final FileEventCallback<T> callback;

        public WatchTask(FileEventCallback<T> callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            for (WatchContext watchContext : watchContextQueue) {
                Invocation invocation = new Invocation(watchContext.interfaceName, "heart", new Class[]{}, null, watchContext.getVersion());
                try {
                    Boolean result = (java.lang.Boolean) new HttpClient().RegisterSend(watchContext.getUrl().getHostname(), watchContext.getUrl().getPort(), invocation);
                    System.out.println(watchContext.getInterfaceName() + " " + result);
                } catch (IOException e) {
                    System.out.println("连接失败" + watchContext.sum + "次");
                    watchContext.sum++;
                    if (watchContext.sum > 3) {
                        unWatch(watchContext);
                    }
                }
            }
        }
    }

    public interface FileEventCallback<T> {
        void OnHeart(File file);
    }

    private class WatchContext {
        private String interfaceName;
        private String version;
        private URL url;
        private int sum;

        public WatchContext(String interfaceName, String version, URL url) {
            this.interfaceName = interfaceName;
            this.version = version;
            this.url = url;
            this.sum = 0;
        }

        public void addSum() {
            sum++;
        }

        public boolean isDie() {
            return sum < 3;
        }

        public String getInterfaceName() {
            return interfaceName;
        }

        public void setInterfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }

        public int getSum() {
            return this.sum;
        }

    }

    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        try {
            FileEventMonitor fileEventMonitor = new FileEventMonitor(500L);
            fileEventMonitor.startWatch(new FileEventMonitor.FileEventCallback() {
                @Override
                public void OnHeart(File file) {

                }
            });
            ss = new ServerSocket(8888);
            while (true) {
                Socket s = ss.accept();
                InputStream in = s.getInputStream();
                OutputStream out = s.getOutputStream();
                DataInputStream dis = new DataInputStream(in);
                DataOutputStream dos = new DataOutputStream(out);

                String interfaceName = dis.readUTF();
                String version = dis.readUTF();

                URL url = new URL(dis.readUTF(), dis.readInt());
                fileEventMonitor.watch(interfaceName, version, url);
                dos.flush();
                s.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ss.close();
        }
    }


}
