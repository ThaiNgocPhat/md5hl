package ra.md5.domain.order.dto.res.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.order.dto.req.user.OrderDto;
import ra.md5.domain.order.dto.req.user.OrderSerialNumberDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderGetBySerialNumberResponse {
    int code;
    HttpStatus message;
    OrderSerialNumberDto data;
}
