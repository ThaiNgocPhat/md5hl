package ra.md5.domain.user.dto.res.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.user.dto.req.user.UserDetailsDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsResponse {
    int code;
    HttpStatus message;
    UserDetailsDto data;
}
