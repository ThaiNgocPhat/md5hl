package ra.md5.domain.user.dto.req.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.role.dto.RoleDto;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String userId;
    String username;
    String email;
    String firstName;
    String lastName;
    String avatar;
    String phone;
    String address;
    boolean status;
    List<RoleDto> roles;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
    boolean deleted;
}
