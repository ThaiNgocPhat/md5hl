package ra.md5.domain.category.dto.res.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.category.entity.Category;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryListResponse {
    int code;
    HttpStatus message;
    long totalElements;
    int totalPages;
    int page;
    int size;
    List<Category> data;
}
