package ra.md5.domain.product.service.manager;

import org.springframework.web.multipart.MultipartFile;
import ra.md5.domain.product.dto.req.manager.ProductAddDto;
import ra.md5.domain.product.dto.req.manager.ProductUpdateDto;
import ra.md5.domain.product.dto.res.manager.ProductAddResponse;
import ra.md5.domain.product.dto.res.manager.ProductDeleteResponse;
import ra.md5.domain.product.dto.res.manager.ProductUpdateResponse;

public interface ProductServiceManager {
    ProductAddResponse createProduct(ProductAddDto request);
    ProductUpdateResponse updateProduct(Integer productId , ProductUpdateDto request);
    ProductDeleteResponse deleteProduct(Integer productId);
}
