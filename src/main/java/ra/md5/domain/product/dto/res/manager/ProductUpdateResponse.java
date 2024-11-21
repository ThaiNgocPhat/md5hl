package ra.md5.domain.product.dto.res.manager;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.product.entity.Product;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateResponse {
    int code;
    HttpStatus message;
    Product data;
}
