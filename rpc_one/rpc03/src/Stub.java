import com.ouliang.common.IUserService;
import com.ouliang.common.User;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.Socket;

// 代理,隐藏细节
public class Stub {
    public static IUserService getStub() {
        InvocationHandler h = (proxy, method, args) -> {
            Socket s = new Socket("localhost", 8888);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(123);

            s.getOutputStream().write(baos.toByteArray());
            s.getOutputStream().flush();

            DataInputStream dis = new DataInputStream(s.getInputStream());
            int id = dis.readInt();
            String name = dis.readUTF();
            User user = new User(id, name);

            dos.close();
            s.close();
            return user;
        };

        Object o = Proxy.newProxyInstance(IUserService.class.getClassLoader(), new Class[]{IUserService.class}, h);
        return (IUserService) o;
    }
}
