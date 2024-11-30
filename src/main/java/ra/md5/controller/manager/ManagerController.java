package ra.md5.controller.manager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md5.domain.category.dto.req.manager.CategoryAddDto;
import ra.md5.domain.category.dto.req.manager.CategoryUpdateDto;
import ra.md5.domain.category.dto.res.manager.CategoryDeleteResponse;
import ra.md5.domain.category.dto.res.manager.CategoryResponse;
import ra.md5.domain.category.service.CategoryServiceManager;
import ra.md5.domain.order.dto.req.manager.OrderChangeDto;
import ra.md5.domain.order.dto.res.manager.OrderChangeResponse;
import ra.md5.domain.order.service.OrderServiceManager;
import ra.md5.domain.product.dto.req.manager.ProductAddDto;
import ra.md5.domain.product.dto.req.manager.ProductUpdateDto;
import ra.md5.domain.product.dto.res.manager.ProductResponse;
import ra.md5.domain.product.service.ProductServiceManager;
import ra.md5.domain.user.dto.res.manager.UserModificationResponse;
import ra.md5.domain.user.dto.res.manager.UserChangeStatusResponse;
import ra.md5.domain.user.service.UserServiceManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager")
public class ManagerController {
    private final CategoryServiceManager categoryServiceManager;
    private final ProductServiceManager productServiceManager;
    private final UserServiceManager userServiceManager;
    private final OrderServiceManager orderServiceManager;

    // CATEGORY
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> addCategory(@Valid @RequestBody CategoryAddDto request){
        CategoryResponse addCategory = categoryServiceManager.createCategory(request);
        return new ResponseEntity<>(addCategory, HttpStatus.CREATED);
    }
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @PathVariable Integer categoryId,
                                                                 @RequestBody CategoryUpdateDto request){
        CategoryResponse updateCategory = categoryServiceManager.updateCategory(categoryId, request);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }
    @PatchMapping("/categories/{categoryId}/toggle")
    public ResponseEntity<CategoryDeleteResponse> toggleCategory(@PathVariable Integer categoryId) {
        CategoryDeleteResponse response = categoryServiceManager.deleteCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    //PRODUCT
    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@Valid @ModelAttribute ProductAddDto request){
        ProductResponse product = productServiceManager.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@Valid @PathVariable Integer productId, @ModelAttribute ProductUpdateDto request){
        ProductResponse product = productServiceManager.updateProduct(productId, request);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    @PatchMapping("/products/{productId}/toggle")
    public ResponseEntity<?> toggleProduct(@PathVariable Integer productId) {
        productServiceManager.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //USER
    @PostMapping("/users/{userId}/role/{roleId}")
    public ResponseEntity<UserModificationResponse> addRoleToUser(@PathVariable String userId, @PathVariable String roleId) {
        UserModificationResponse response = userServiceManager.addRole(userId, roleId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @DeleteMapping("/users/{userId}/role/{roleId}")
    public ResponseEntity<UserModificationResponse> deleteRoleFromUser(@PathVariable String userId, @PathVariable String roleId) {
        UserModificationResponse response = userServiceManager.deleteRole(userId, roleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserChangeStatusResponse> changeStatus(@PathVariable String userId){
        UserChangeStatusResponse response = userServiceManager.changeStatus(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //ORDER
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderChangeResponse> changeOrderStatus(@PathVariable Integer orderId, @RequestBody OrderChangeDto request){
        OrderChangeResponse order = orderServiceManager.changeStatusOrder(orderId, request);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
