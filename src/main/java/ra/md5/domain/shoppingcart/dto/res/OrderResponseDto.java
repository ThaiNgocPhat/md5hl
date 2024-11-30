package ra.md5.domain.shoppingcart.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.shoppingcart.dto.req.OrderDetailsDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponseDto {
    String serialNumber;
    BigDecimal totalPrice;
    String status;
    String note;
    String receiveName;
    String receivePhone;
    String receiveAddress;
    List<OrderDetailsDto> orderDetails;
}
