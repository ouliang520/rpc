import com.ouliang.common.IUserService;
import com.ouliang.common.User;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static boolean running = true;

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(8888);
        while (running) {
            Socket s = ss.accept();
            process(s);
            s.close();
        }
        ss.close();
    }

    private static void process(Socket s) throws Exception {
        InputStream in = s.getInputStream();
        OutputStream out = s.getOutputStream();
        ObjectInputStream ois = new ObjectInputStream(in);


        String methodName = ois.readUTF();
        Class[] parameterType = (Class[]) ois.readObject();
        Object[] args = (Object[]) ois.readObject();

        IUserService service = new UserServiceImpl();
        Method method = service.getClass().getMethod(methodName,parameterType);
        User user = (User) method.invoke(service,args);

        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(user);
        oos.flush();
    }
}