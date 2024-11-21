package ra.md5.domain.user.dto.res.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseError {
    private int code;
    private HttpStatus message;
    private Object details;
}
