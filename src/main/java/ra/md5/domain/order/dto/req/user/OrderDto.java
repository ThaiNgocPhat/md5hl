package ra.md5.domain.order.dto.req.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;
import ra.md5.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
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
