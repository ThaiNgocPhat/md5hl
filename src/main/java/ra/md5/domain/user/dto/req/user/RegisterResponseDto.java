package ra.md5.domain.user.dto.req.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.role.dto.RoleDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponseDto {
     String username;
     String email;
     String firstName;
     String lastName;
     String phone;
     String address;
     List<RoleDto> authorities;
}
