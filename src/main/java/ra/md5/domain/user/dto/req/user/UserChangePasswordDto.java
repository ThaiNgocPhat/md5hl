package ra.md5.domain.user.dto.req.user;

import jakarta.validation.constraints.NotBlank;
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
public class UserChangePasswordDto {

    @NotBlank(message = "Mật khẩu hiện tại không được để trống")
    @Size(min = 8, max = 50, message = "Mật khẩu hiện tại phải từ 8 đến 50 ký tự")
    String currentPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 8, max = 50, message = "Mật khẩu mới phải từ 8 đến 50 ký tự")
    String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    @Size(min = 8, max = 50, message = "Xác nhận mật khẩu phải từ 8 đến 50 ký tự")
    String confirmPassword;
}

