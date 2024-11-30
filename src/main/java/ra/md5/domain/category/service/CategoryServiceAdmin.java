package ra.md5.domain.category.service;
import ra.md5.domain.category.dto.res.admin.CategoryListResponse;
import ra.md5.domain.category.dto.res.admin.RevenueByCategoryResponse;
import ra.md5.domain.category.dto.res.manager.CategoryResponse;
import ra.md5.domain.category.entity.Category;

public interface CategoryServiceAdmin {
    CategoryListResponse getCategory(int page, int size, String sort, String direction);
    CategoryResponse getCategoryById(Integer categoryId);
    RevenueByCategoryResponse getRevenueByCategory();
}
