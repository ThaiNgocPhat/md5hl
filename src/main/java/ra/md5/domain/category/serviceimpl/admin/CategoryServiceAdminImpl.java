package ra.md5.domain.category.serviceimpl.admin;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.category.dto.res.admin.CategoryListResponse;
import ra.md5.domain.category.dto.res.manager.CategoryResponse;
import ra.md5.domain.category.entity.Category;
import ra.md5.domain.category.exception.CategoryNotFoundException;
import ra.md5.domain.category.repository.CategoryRepository;
import ra.md5.domain.category.service.admin.CategoryServiceAdmin;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

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
        response.setData(categoryList);
        return response;
    }

    @Override
    public CategoryResponse<Category> getCategoryById(Integer categoryId) {
        //Kiểm tra id có tồn tại không
        Category category = categoryRepository.findById(categoryId)
               .orElseThrow(() -> new CategoryNotFoundException("Danh mục không tồn tại"));
        //Thông báo trả về
        CategoryResponse<Category> response = new CategoryResponse<>();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(category);
        return response;
    }
}
