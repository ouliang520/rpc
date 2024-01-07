import com.ouliang.common.IProductService;
import com.ouliang.common.IUserService;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        IUserService service01 = (IUserService) Stub.getStub(IUserService.class);
        System.out.println(service01.findUserByID(123));

        IProductService service02 = (IProductService)Stub.getStub(IProductService.class);
        System.out.println(service02.findProductByID(33));
    }
}