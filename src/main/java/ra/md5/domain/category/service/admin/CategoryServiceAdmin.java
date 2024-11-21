package ra.md5.domain.category.service.admin;
import ra.md5.domain.category.dto.res.admin.CategoryListResponse;
import ra.md5.domain.category.dto.res.manager.CategoryResponse;
import ra.md5.domain.category.entity.Category;

public interface CategoryServiceAdmin {
    CategoryListResponse getCategory(int page, int size, String sort, String direction);
    CategoryResponse<Category> getCategoryById(Integer categoryId);
}
