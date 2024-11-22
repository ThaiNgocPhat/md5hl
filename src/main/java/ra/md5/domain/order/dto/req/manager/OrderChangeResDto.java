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
    Integer orderId;  // ID đơn hàng
    String serialNumber;  // Mã đơn hàng
    BigDecimal totalPrice;  // Tổng giá trị đơn hàng
    OrderStatus status;  // Trạng thái đơn hàng
    String receiveName;  // Tên người nhận
    String receivePhone;  // Số điện thoại người nhận
    String receiveAddress;  // Địa chỉ người nhận
    LocalDateTime createdAt;  // Ngày tạo đơn hàng
    LocalDateTime receivedAt; //Ngày giao hàng
}
