import com.ouliang.common.IUserService;
import com.ouliang.common.User;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.Socket;

// 代理,隐藏细节
public class Stub {
    public static IUserService getStub() {
        InvocationHandler h = (proxy, method, args) -> {
            Socket s = new Socket("localhost", 8888);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

            String methodName = method.getName(); //方法名
            Class[] parametersTypes = method.getParameterTypes(); //参数类型
            oos.writeUTF(methodName);
            oos.writeObject(parametersTypes);
            oos.writeObject(args);
            oos.flush();


            DataInputStream dis = new DataInputStream(s.getInputStream());
            int id = dis.readInt();
            String name = dis.readUTF();
            User user = new User(id, name);


            oos.close();
            s.close();
            return user;
        };

        Object o = Proxy.newProxyInstance(IUserService.class.getClassLoader(), new Class[]{IUserService.class}, h);
        return (IUserService) o;
    }
}
