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
public class WishListDto {
    Integer wishListId;
    String sku;
    String productName;
    String description;
    BigDecimal unitPrice;
    Integer stock;
    String image;
}
