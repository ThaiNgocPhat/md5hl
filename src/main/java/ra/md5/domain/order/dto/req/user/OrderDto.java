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
    Integer orderId;  // ID đơn hàng
    String serialNumber;  // Mã đơn hàng
    BigDecimal totalPrice;  // Tổng giá trị đơn hàng
    OrderStatus status;  // Trạng thái đơn hàng
    String receiveName;  // Tên người nhận
    String receivePhone;  // Số điện thoại người nhận
    String receiveAddress;  // Địa chỉ người nhận
    LocalDateTime createdAt;  // Ngày tạo đơn hàng
    LocalDateTime receivedAt; // Ngày giao hàng
}
