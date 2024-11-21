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
public class LoginDto {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 6, max = 100, message = "Không được nhỏ hơn 6 và lớn hơn 100")
    String username;

    @NotBlank(message = "Không được để trống")
    @Size(min = 8, max = 100, message = "Mật khẩu phải lớn hơn 8 và nhỏ hơn 50")
    String password;
}
