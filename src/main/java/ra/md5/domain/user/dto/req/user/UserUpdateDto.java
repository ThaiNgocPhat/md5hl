package ra.md5.domain.user.dto.req.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import ra.md5.domain.user.validation.PhoneUnique;
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateDto {

    @NotBlank(message = "Email không được để trống")
    @Size(min = 10, max = 50, message = "Email phải lớn hơn 10 và nhỏ hơn 50")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email không hợp lệ")
    String email;

    @NotBlank(message = "Firstname không được để trống")
    @Size(min = 3, max = 50, message = "Không được nhỏ hơn 3 và lớn hơn 50")
    String firstName;

    @NotBlank(message = "Lastname không được để trống")
    @Size(min = 3, max = 50, message = "Không được nhỏ hơn 3 và lớn hơn 50")
    String lastName;

    @NotNull(message = "Không được để trống")
    @Pattern(regexp = "^(0[3|5|7|8|9]\\d{8}|01[0-9]\\d{7})$", message = "Số điện thoại pải có 10 chữ số và bắt đầu bằng 0 và 3, 5, 7,9")
    @PhoneUnique(message = "Số điện thoại đã tồn tại")
    String phone;

    MultipartFile avatar;
}
