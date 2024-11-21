package ra.md5.domain.category.serviceimpl.manager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.category.dto.req.manager.CategoryAddDto;
import ra.md5.domain.category.dto.req.manager.CategoryUpdateDto;
import ra.md5.domain.category.dto.res.manager.CategoryDeleteResponse;
import ra.md5.domain.category.dto.res.manager.CategoryResponse;
import ra.md5.domain.category.dto.res.manager.CategoryUpdateResponse;
import ra.md5.domain.category.entity.Category;
import ra.md5.domain.category.exception.CategoryNotFoundException;
import ra.md5.domain.category.repository.CategoryRepository;
import ra.md5.domain.category.service.manager.CategoryServiceManager;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryServiceManagerImpl implements CategoryServiceManager {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public CategoryResponse<Category> createCategory(CategoryAddDto request) {
        //Kiểm tra tồn tại hay chưa
        if (categoryRepository.existsByCategoryName(request.getCategoryName())){
            throw new CategoryNotFoundException("Danh mục đã tồn tại");
        }
        //Ánh xạ từ DTO về Entity
        Category category = modelMapper.map(request, Category.class);
        //Thêm mới vào cơ sở dữ liệu
        Category addCategory = categoryRepository.save(category);
        //Trả về thông báo
        CategoryResponse<Category> response = new CategoryResponse<>();
        response.setCode(201);
        response.setMessage(HttpStatus.CREATED);
        response.setData(addCategory);
        return response;
    }
    @Override
    public CategoryUpdateResponse updateCategory(Integer categoryId, CategoryUpdateDto request) {
        // Kiểm tra id có tồn tại không
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Danh mục không tồn tại"));
        // Sử dụng ModelMapper để ánh xạ DTO sang Entity
        ModelMapper mapper = new ModelMapper();
        mapper.map(request, category);
        // Lưu vào cơ sở dữ liệu
        Category updatedCategory = categoryRepository.save(category);
        // Tạo response trả về
        CategoryUpdateResponse response = new CategoryUpdateResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(updatedCategory);
        return response;
    }
    @Override
    public CategoryDeleteResponse deleteCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục!"));
        // Thay đổi trạng thái xóa
        category.setDeleted(!category.isDeleted()); // Đảo trạng thái isDeleted (true -> false, false -> true)
        category.setUpdatedAt(LocalDateTime.now()); // Cập nhật thời gian thay đổi
        categoryRepository.save(category); // Lưu thay đổi vào database
        // Thông báo trả về
        String action = category.isDeleted() ? " đã ẩn " : " đã hiện ";
        CategoryDeleteResponse response = new CategoryDeleteResponse();
        response.setCode(200);
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Danh mục " + categoryId + action + " thành công");
        return response;
    }
}
