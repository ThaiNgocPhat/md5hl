package ra.md5.domain.shoppingcart.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartListCartDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartListCartResponse {
    int code;
    HttpStatus message;
    List<ShoppingCartListCartDto> data;
}
