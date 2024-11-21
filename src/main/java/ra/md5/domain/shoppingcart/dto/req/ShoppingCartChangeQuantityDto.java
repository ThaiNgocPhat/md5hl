package ra.md5.domain.shoppingcart.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoppingCartChangeQuantityDto {
    @NotNull(message = "Không được để trống")
    @Min(value = 1)
    private Integer orderQuantity;
}
