package ra.md5.domain.shoppingcart.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartCheckoutDto {
    String note;
    String receiveName;
    String receivePhone;
    String receiveAddress;
}
