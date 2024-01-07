import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.Socket;

// 代理,隐藏细节
public class Stub {
    public static Object getStub(Class clazz) {
        InvocationHandler h = (proxy, method, args) -> {
            Socket s = new Socket("localhost", 8888);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            String clazzName = clazz.getName(); //调用的接口名
            String methodName = method.getName(); //方法名
            Class[] parametersTypes = method.getParameterTypes(); //参数类型
            oos.writeUTF(clazzName);
            oos.writeUTF(methodName);
            oos.writeObject(parametersTypes);
            oos.writeObject(args);
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            Object o = ois.readObject();

            oos.close();
            s.close();
            return o;
        };

        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, h);
        return o;
    }
}
