package ra.md5.domain.shoppingcart.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartAddDto {
    @NotNull(message = "Product ID không được để trống")
    @Positive(message = "Product ID phải là số dương")
    int productId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1)
    @Positive(message = "Số lượng phải lớn hơn 0 và tối thiểu là 1")
    int orderQuantity;
}
