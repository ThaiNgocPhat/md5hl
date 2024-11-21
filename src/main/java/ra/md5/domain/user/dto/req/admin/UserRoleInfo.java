package ra.md5.domain.user.dto.req.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.role.entity.Role;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoleInfo {
    String username;
    List<Role> roles;
}
