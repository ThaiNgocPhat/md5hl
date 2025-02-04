package ra.md5.domain.product.dto.res.permitall;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.product.dto.req.permitall.BestSellerProduct;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BestSellerProductResponse {
    int code;
    HttpStatus message;
    List<BestSellerProduct> data;
}
