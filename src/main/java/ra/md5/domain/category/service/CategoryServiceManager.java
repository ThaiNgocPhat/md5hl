package ra.md5.domain.category.service;
import ra.md5.domain.category.dto.req.manager.CategoryAddDto;
import ra.md5.domain.category.dto.req.manager.CategoryUpdateDto;
import ra.md5.domain.category.dto.res.manager.CategoryDeleteResponse;
import ra.md5.domain.category.dto.res.manager.CategoryResponse;

public interface CategoryServiceManager {
    CategoryResponse createCategory(CategoryAddDto request);
    CategoryResponse updateCategory(Integer categoryId, CategoryUpdateDto request);
    CategoryDeleteResponse deleteCategory(Integer categoryId);
}
