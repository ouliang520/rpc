import com.ouliang.common.User;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Stub stub = new Stub();
        System.out.println(Stub.findUserByID(123));
    }
}