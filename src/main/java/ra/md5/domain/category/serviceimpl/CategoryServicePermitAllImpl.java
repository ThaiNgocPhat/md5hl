package ra.md5.domain.category.serviceimpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.category.dto.req.permitall.CategoryListDto;
import ra.md5.domain.category.dto.res.permitall.CategoryResponse;
import ra.md5.domain.category.entity.Category;
import ra.md5.domain.category.repository.CategoryRepository;
import ra.md5.domain.category.service.CategoryServicePermitAll;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServicePermitAllImpl implements CategoryServicePermitAll {
    private final CategoryRepository categoryRepository;
    @Override
    public CategoryResponse getAllCategory() {
        // Lấy tất cả các danh mục từ repository (cơ sở dữ liệu)
        List<Category> categories = categoryRepository.findAll();
        // Chuyển đổi các danh mục thành danh sách CategoryListDto
        List<CategoryListDto> categoryListDtos = categories.stream()
                .filter(category -> category.isStatus() && !category.isDeleted())
                .map(category -> new CategoryListDto(category.getCategoryId(), category.getCategoryName(), category.getDescription()))
                .collect(Collectors.toList());

        // Tạo CategoryResponse để trả về
        CategoryResponse response = new CategoryResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(categoryListDtos);
        return response;
    }
}
