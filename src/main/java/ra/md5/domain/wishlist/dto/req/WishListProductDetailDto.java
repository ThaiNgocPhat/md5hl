package ra.md5.domain.wishlist.dto.req;

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
public class WishListProductDetailDto {
    Integer wishListId;
    String sku;
    String productName;
    BigDecimal unitPrice;
    String description;
    Integer stock;
    String image;
}
