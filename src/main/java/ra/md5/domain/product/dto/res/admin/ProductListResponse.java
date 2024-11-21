package ra.md5.domain.product.dto.res.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import ra.md5.domain.product.entity.Product;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductListResponse {
    private int code;
    private HttpStatus message;
    private long totalElements;
    private int totalPages;
    private List<Product> data;
}
