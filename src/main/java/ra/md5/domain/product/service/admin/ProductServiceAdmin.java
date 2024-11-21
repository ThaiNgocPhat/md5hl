package ra.md5.domain.product.service.admin;
import ra.md5.domain.product.dto.res.admin.ProductFindByIdResponse;
import ra.md5.domain.product.dto.res.admin.ProductListResponse;

import java.util.List;

public interface ProductServiceAdmin {
    ProductListResponse listProduct(int page, int size, String sort, String direction);
    ProductFindByIdResponse getProductById(Integer productId);
}
