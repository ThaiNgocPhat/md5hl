package ra.md5.domain.user.dto.req.manager;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.role.dto.RoleDto;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserModificationDto {
    String username;
    String email;
    String firstName;
    String lastName;
    String avatar;
    String phone;
    String address;
    List<RoleDto> data;
}
