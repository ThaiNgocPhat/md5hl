package ra.md5.domain.product.dto.res.manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import ra.md5.domain.product.entity.Product;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductAddResponse {
    private int code;
    private HttpStatus message;
    private Product data;
}
