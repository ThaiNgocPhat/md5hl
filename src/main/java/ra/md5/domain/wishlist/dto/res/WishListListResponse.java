package ra.md5.domain.wishlist.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.wishlist.dto.req.WishListProductDetailDto;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListListResponse {
    int code;
    HttpStatus message;
    List<WishListProductDetailDto> data;
}
