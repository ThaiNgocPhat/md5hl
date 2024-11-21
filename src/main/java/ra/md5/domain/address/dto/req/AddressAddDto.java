package ra.md5.domain.address.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressAddDto {

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(min = 8, max = 255, message = "Địa chỉ phải từ 8 đến 255 ký tự")
    String address;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[3|5|7|8|9]\\d{8}|01[0-9]\\d{7})$",
            message = "Số điện thoại phải có 10 chữ số và bắt đầu bằng 03, 05, 07, 08, hoặc 09")
    String phone;

    @NotBlank(message = "Tên người nhận không được để trống")
    @Size(min = 3, max = 50, message = "Tên người nhận phải từ 3 đến 50 ký tự")
    String receiveName;
}
