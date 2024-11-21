package ra.md5.domain.product.dto.req.permitall;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductNewDto {
    String sku;
    String productName;
    String description;
    BigDecimal price;
    Integer quantity;
    String image;
}
