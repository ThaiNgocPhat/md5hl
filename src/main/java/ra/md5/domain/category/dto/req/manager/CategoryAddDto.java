package ra.md5.domain.category.dto.req.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.category.validation.CategoryNameUnique;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryAddDto {

    @NotBlank(message = "Tên danh mục không dược để trống")
    @CategoryNameUnique(message = "Danh mục đã tồn tại")
    @Size(min = 3, max = 100, message = "Thể loại không được nhỏ hơn 3 và lớn hơn 100")
    String categoryName;

    @NotBlank(message = "Mô tả không thể để trống")
    @Size(min = 10, max = 100, message = "Mô tả phải lớn hơn 10 và nhỏ hơn 100")
    String description;
}
