package ra.md5.domain.category.serviceimpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.category.dto.req.admin.CategoryRevenueData;
import ra.md5.domain.category.dto.res.admin.CategoryListResponse;
import ra.md5.domain.category.dto.res.admin.RevenueByCategoryResponse;
import ra.md5.domain.category.dto.res.manager.CategoryResponse;
import ra.md5.domain.category.entity.Category;
import ra.md5.domain.category.exception.CategoryNotFoundException;
import ra.md5.domain.category.repository.CategoryRepository;
import ra.md5.domain.category.service.CategoryServiceAdmin;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryListResponse getCategory(int page, int size, String sort, String direction) {
        //Sắp xếp
        Sort sortCategory = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        //Phân trang
        Pageable pageable = PageRequest.of(page, size, sortCategory);
        //Lấy danh sách danh mục từ repository
        Page<Category> categories = categoryRepository.findAll(pageable);
        //Lấy danh sách từ page
        List<Category> categoryList = categories.getContent();
        //Thông báo trả về
        CategoryListResponse response = new CategoryListResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setTotalElements(categories.getTotalElements());
        response.setTotalPages(categories.getTotalPages());
        response.setPage(page);
        response.setSize(size);
        response.setData(categoryList);
        return response;
    }

    @Override
    public CategoryResponse getCategoryById(Integer categoryId) {
        //Kiểm tra id có tồn tại không
        Category category = categoryRepository.findById(categoryId)
               .orElseThrow(() -> new CategoryNotFoundException("Danh mục không tồn tại"));
        //Thông báo trả về
        CategoryResponse response = new CategoryResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(category);
        return response;
    }

    @Override
    public RevenueByCategoryResponse getRevenueByCategory() {
        List<Object[]> result = categoryRepository.findRevenueByCategory();
        List<CategoryRevenueData> data = result.stream()
                .map(row -> {
                    CategoryRevenueData categoryRevenue = new CategoryRevenueData();

                    // Cast categoryId directly to Integer (no need for String conversion)
                    categoryRevenue.setCategoryId((Integer) row[0]);  // Correct cast

                    // Ensure categoryName is cast to String
                    categoryRevenue.setCategoryName((String) row[1]);

                    // Cast revenue to Double (or you can use BigDecimal depending on how the data is stored)
                    categoryRevenue.setRevenue(((Number) row[2]).doubleValue()); // Safely cast to double

                    return categoryRevenue;
                })
                .collect(Collectors.toList());
        RevenueByCategoryResponse response = new RevenueByCategoryResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(data);
        return response;
    }
}
