package ra.md5.domain.user.dto.res.manager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.role.dto.RoleDto;
import ra.md5.domain.user.dto.req.manager.UserModificationDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserModificationResponse {
    int code;
    HttpStatus message;
    UserModificationDto data;
}
