package ra.md5.domain.product.service;
import ra.md5.domain.product.dto.res.permitall.*;


public interface ProductServicePermitAll {
    ProductResponse searchProduct(String keyword);

    ProductListResponse listProduct(int page, int size, String sort, String direction);

    ProductFindByIdResponse findProductById(Integer productId);

    CategoryForProductResponse getAllCategoryForProduct(Integer categoryId);

    ProductResponse getNewProduct();

    BestSellerProductResponse getTopSellingProducts();
}
