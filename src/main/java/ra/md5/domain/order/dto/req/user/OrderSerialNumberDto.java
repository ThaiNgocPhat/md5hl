package ra.md5.domain.order.dto.req.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSerialNumberDto {
    private String serialNumber;
    private BigDecimal totalPrice;
    private String status;
    private String receiveName;
    private String receivePhone;
    private String receiveAddress;
    private List<OrderDetailsDto> orderDetails;
}
