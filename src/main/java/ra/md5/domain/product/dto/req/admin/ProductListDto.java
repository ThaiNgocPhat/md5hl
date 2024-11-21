package ra.md5.domain.product.dto.req.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductListDto {
    Integer productId;
    String sku;
    String productName;
    String description;
    BigDecimal unitPrice;
    Integer stock;
    String image;
    Boolean status;
    Integer categoryId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
