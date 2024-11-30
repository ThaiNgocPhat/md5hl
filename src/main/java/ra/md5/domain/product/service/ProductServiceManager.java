package ra.md5.domain.product.service;
import ra.md5.domain.product.dto.req.manager.ProductAddDto;
import ra.md5.domain.product.dto.req.manager.ProductUpdateDto;
import ra.md5.domain.product.dto.res.manager.ProductResponse;

public interface ProductServiceManager {
    ProductResponse createProduct(ProductAddDto request);
    ProductResponse updateProduct(Integer productId , ProductUpdateDto request);
    void deleteProduct(Integer productId);
}
