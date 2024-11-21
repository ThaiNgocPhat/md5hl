package ra.md5.domain.product.dto.res.permitall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import ra.md5.domain.product.dto.req.permitall.ProductFindByIdDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFindByIdResponse {
    private int code;
    private HttpStatus message;
    private ProductFindByIdDto data;
}
