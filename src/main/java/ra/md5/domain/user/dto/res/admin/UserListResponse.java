package ra.md5.domain.user.dto.res.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.user.dto.req.user.UserDto;
import ra.md5.domain.user.entity.User;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserListResponse {
    int code;
    HttpStatus message;
    long totalElements;
    int totalPages;
    int page;
    int size;
    List<UserDto> data;
}
