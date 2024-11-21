package ra.md5.domain.product.service.permitall;

import ra.md5.domain.product.dto.res.permitall.*;

public interface ProductServicePermitAll {
    ProductSearchResponse searchProduct(String keyword);
    ProductListResponse listProduct(int page, int size, String sort, String direction);
    ProductFindByIdResponse findProductById(Integer productId);
    ProductForCategoryResponse getAllCategoryForProduct(Integer categoryId);
    ProductNewResponse getNewProduct();
}
