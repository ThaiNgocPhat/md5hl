package ra.md5.domain.product.dto.req.manager;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import ra.md5.domain.product.validation.ProductNameUnique;
import ra.md5.domain.product.validation.SkuUnique;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAddDto {

    @SkuUnique(message = "SKU đã tồn tại")
    @NotBlank(message = "Không được để trống")
    @Size(min = 5, max = 15, message = "SKU phải có độ dài từ 5 đến 15 ký tự")
    @Pattern(regexp = "^CT-\\d{4}$", message = "SKU phải bắt đầu bằng 'CT-' và theo sau là 4 chữ số")
    String sku;

    @NotBlank(message = "Không được để trống")
    @ProductNameUnique(message = "Tên sản phẩm đã tồn tại")
    @Size(min = 5, max = 100, message = "Độ dài phải lớn hơn 5 và nhỏ hơn 100")
    String productName;

    @NotBlank(message = "Không được để trống")
    @Size(min = 10, max = 255, message = "Mô tả phải có độ dài từ 10 đến 255 ký tự")
    String description;

    @NotNull(message = "Không được để trống")
    @DecimalMin(value = "0.00", message = "Giá phải lớn hơn hoặc bằng 0.00")
    @DecimalMax(value = "99999999.99", message = "Giá phải nhỏ hơn hoặc bằng 99999999.99")
    BigDecimal unitPrice;

    @NotNull(message = "Không được để trống")
    @Min(value = 1)
    Integer stock;

    MultipartFile image;

    @NotNull(message = "Không được để trống")
    Integer categoryId;
}
