package ra.md5.domain.order.dto.req.manager;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderChangeResDto {
    Integer orderId;
    String serialNumber;
    BigDecimal totalPrice;
    OrderStatus status;
    String receiveName;
    String receivePhone;
    String receiveAddress;
    LocalDateTime createdAt;
    LocalDateTime receivedAt;
}
