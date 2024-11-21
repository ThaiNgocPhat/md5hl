package ra.md5.domain.category.dto.res.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import ra.md5.domain.category.entity.Category;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryListResponse {
    private int code;
    private HttpStatus message;
    private long totalElements;
    private int totalPages;
    private List<Category> data;
}
