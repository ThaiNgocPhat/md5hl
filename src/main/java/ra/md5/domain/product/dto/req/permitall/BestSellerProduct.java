package ra.md5.domain.product.dto.req.permitall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BestSellerProduct {
    Integer productId;
    String productName;
    BigDecimal price;
    Integer soldCount;
}
