package ra.md5.domain.order.dto.req.admin;

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
public class OrderResDto {
    Integer orderId;
    BigDecimal totalPrice;
    String status;
    String note;
    String receiveName;
    String receiveAddress;
    String receivePhone;
    LocalDateTime createdAt;
    String username;
}
