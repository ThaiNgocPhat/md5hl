package ra.md5.domain.category.service.manager;

import ra.md5.domain.category.dto.req.manager.CategoryAddDto;
import ra.md5.domain.category.dto.req.manager.CategoryUpdateDto;
import ra.md5.domain.category.dto.res.manager.CategoryDeleteResponse;
import ra.md5.domain.category.dto.res.manager.CategoryResponse;
import ra.md5.domain.category.dto.res.manager.CategoryUpdateResponse;
import ra.md5.domain.category.entity.Category;

public interface CategoryServiceManager {
    CategoryResponse<Category> createCategory(CategoryAddDto request);
    CategoryUpdateResponse updateCategory(Integer categoryId, CategoryUpdateDto request);
    CategoryDeleteResponse deleteCategory(Integer categoryId);
}
