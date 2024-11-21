package ra.md5.domain.product.dto.req.permitall;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSearchDto {
    String productName;
    String description;
    BigDecimal unitPrice;
    Integer stock;
    String image;
    int categoryId;
}
