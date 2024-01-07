import com.ouliang.common.IUserService;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        IUserService service = (IUserService) Stub.getStub(IUserService.class);
        System.out.println(service.findUserByID(123));
    }
}