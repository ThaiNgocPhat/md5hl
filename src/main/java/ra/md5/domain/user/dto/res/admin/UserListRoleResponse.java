package ra.md5.domain.user.dto.res.admin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.user.dto.req.admin.UserRoleInfo;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserListRoleResponse {
    int code;
    HttpStatus message;
    String username;
    List<UserRoleInfo> data;
}
