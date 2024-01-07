import com.ouliang.common.IProductService;
import com.ouliang.common.Product;

public class ProductServiceImpl implements IProductService {
    @Override
    public Product findProductByID(Integer id) {
        return new Product(id, "产品");
    }
}
