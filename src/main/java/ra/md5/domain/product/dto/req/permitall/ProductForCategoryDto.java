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
public class ProductForCategoryDto {
    String sku;
    String productName;
    String description;
    Integer stock;
    BigDecimal unitPrice;
    String image;
    int categoryId;
}