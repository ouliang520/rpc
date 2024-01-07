import com.ouliang.common.IUserService;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        IUserService service = Stub.getStub();
        System.out.println(service.findUserByID(123));
    }
}