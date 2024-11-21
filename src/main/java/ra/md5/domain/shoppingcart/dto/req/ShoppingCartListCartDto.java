package ra.md5.domain.shoppingcart.dto.req;

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
public class ShoppingCartListCartDto {
    Integer shoppingCartId;
    String productName;
    Integer orderQuantity;
    BigDecimal totalPrice;
}
