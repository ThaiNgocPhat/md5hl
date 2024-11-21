package ra.md5.controller.permitall;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md5.domain.category.dto.res.permitall.CategoryResponse;
import ra.md5.domain.category.service.permitall.CategoryServicePermitAll;
import ra.md5.domain.product.dto.res.permitall.*;
import ra.md5.domain.product.service.permitall.ProductServicePermitAll;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PermitAllController {
    private final CategoryServicePermitAll categoryServicePermitAll;
    private final ProductServicePermitAll productServicePermitAll;

    @GetMapping("/categories")
    public ResponseEntity<CategoryResponse> getAllCategory(){
        CategoryResponse categoryPermitAll = categoryServicePermitAll.getAllCategory();
        return new ResponseEntity<>(categoryPermitAll, HttpStatus.OK);
    }
    @GetMapping("/products/search")
    public ResponseEntity<ProductSearchResponse> searchProduct(@RequestParam String keywords){
        ProductSearchResponse productSearchResponse = productServicePermitAll.searchProduct(keywords);
        return new ResponseEntity<>(productSearchResponse, HttpStatus.OK);
    }
    @GetMapping("/products")
    public ResponseEntity<ProductListResponse> listProduct(@RequestParam int page,
                                                           @RequestParam int size,
                                                           @RequestParam String sort,
                                                           @RequestParam String direction){
        ProductListResponse productListResponse = productServicePermitAll.listProduct(page, size, sort, direction);
        return new ResponseEntity<>(productListResponse, HttpStatus.OK);
    }
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductFindByIdResponse> findProductById(@PathVariable Integer productId){
        ProductFindByIdResponse productSearchResponse = productServicePermitAll.findProductById(productId);
        return new ResponseEntity<>(productSearchResponse, HttpStatus.OK);
    }
    @GetMapping("/products/categories/{categoryId}")
    public ResponseEntity<ProductForCategoryResponse> getProductByCategory(@PathVariable Integer categoryId){
        ProductForCategoryResponse productForCategoryResponse = productServicePermitAll.getAllCategoryForProduct(categoryId);
        return new ResponseEntity<>(productForCategoryResponse, HttpStatus.OK);
    }
    @GetMapping("/products/new-products")
    public ResponseEntity<ProductNewResponse> getNewProduct(){
        ProductNewResponse productNewResponse = productServicePermitAll.getNewProduct();
        return new ResponseEntity<>(productNewResponse, HttpStatus.OK);
    }
}
