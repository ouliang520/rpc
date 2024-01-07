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
        ObjectInputStream oos = new ObjectInputStream(in);
        DataOutputStream dos = new DataOutputStream(out);

        String methodName = oos.readUTF();
        Class[] parameterType = (Class[]) oos.readObject();
        Object[] args = (Object[]) oos.readObject();

        IUserService service = new UserServiceImpl();
        Method method = service.getClass().getMethod(methodName,parameterType);
        User user = (User) method.invoke(service,args);

        dos.writeInt(user.getId());
        dos.writeUTF(user.getName());
        dos.flush();
    }
}