import com.ouliang.common.IUserService;
import com.ouliang.common.User;

public class UserServiceImpl implements IUserService {
    @Override
    public User findUserByID(Integer id) {
        return new User(id,"Ouliang");
    }

}
