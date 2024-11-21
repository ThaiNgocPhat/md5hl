package ra.md5.controller.admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md5.domain.product.dto.res.admin.ProductFindByIdResponse;
import ra.md5.domain.product.dto.res.admin.ProductListResponse;
import ra.md5.domain.product.service.admin.ProductServiceAdmin;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/products")
public class ProductController {
    private final ProductServiceAdmin productServiceAdmin;
    @GetMapping
    public ResponseEntity<ProductListResponse> listProduct(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "2") int size,
                                                           @RequestParam(defaultValue = "productName") String sort,
                                                           @RequestParam(defaultValue = "desc") String direction){
        ProductListResponse products = productServiceAdmin.listProduct(page, size, sort, direction);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductFindByIdResponse> getProductById(@PathVariable Integer productId){
        ProductFindByIdResponse getProductById = productServiceAdmin.getProductById(productId);
        return new ResponseEntity<>(getProductById, HttpStatus.OK);
    }
}
