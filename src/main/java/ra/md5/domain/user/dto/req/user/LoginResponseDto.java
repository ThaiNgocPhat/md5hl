package ra.md5.domain.user.dto.req.user;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.role.dto.RoleDto;
import ra.md5.domain.role.entity.Role;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponseDto {
    String token;
    String username;
    String avatar;
    boolean status;
    List<RoleDto> roles;
}
