package ra.md5.domain.product.service;
import ra.md5.domain.product.dto.req.admin.BestSellerRequest;
import ra.md5.domain.product.dto.req.admin.MostLikedRequest;
import ra.md5.domain.product.dto.res.admin.BestSellerResponse;
import ra.md5.domain.product.dto.res.admin.MostLikedResponse;
import ra.md5.domain.product.dto.res.admin.ProductFindByIdResponse;
import ra.md5.domain.product.dto.res.admin.ProductListResponse;

import java.util.List;

public interface ProductServiceAdmin {
    ProductListResponse listProduct(int page, int size, String sort, String direction);
    ProductFindByIdResponse getProductById(Integer productId);
    BestSellerResponse getBestSellerProducts(BestSellerRequest request);
    MostLikedResponse getMostLikedProducts(MostLikedRequest request);
}
