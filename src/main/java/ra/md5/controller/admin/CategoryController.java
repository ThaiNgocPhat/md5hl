package ra.md5.controller.admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md5.domain.category.dto.res.admin.CategoryListResponse;
import ra.md5.domain.category.dto.res.admin.RevenueByCategoryResponse;
import ra.md5.domain.category.dto.res.manager.CategoryResponse;
import ra.md5.domain.category.service.CategoryServiceAdmin;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
public class CategoryController {
    private final CategoryServiceAdmin categoryServiceAdmin;

    @GetMapping
    public ResponseEntity<CategoryListResponse> getAllCategories(@RequestParam int page,
                                                                 @RequestParam int size,
                                                                 @RequestParam String sort,
                                                                 @RequestParam String direction){
        CategoryListResponse categories = categoryServiceAdmin.getCategory(page, size, sort, direction);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer categoryId){
        CategoryResponse category = categoryServiceAdmin.getCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    @GetMapping("/reports/revenue-by-category")
    public ResponseEntity<RevenueByCategoryResponse> getRevenueByCategory() {
        RevenueByCategoryResponse response = categoryServiceAdmin.getRevenueByCategory();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
