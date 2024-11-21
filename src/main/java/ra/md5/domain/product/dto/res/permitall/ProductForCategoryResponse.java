package ra.md5.domain.product.dto.res.permitall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import ra.md5.domain.product.dto.req.permitall.ProductForCategoryDto;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductForCategoryResponse {
    private int code;
    private HttpStatus message;
    private List<ProductForCategoryDto> data;
}
