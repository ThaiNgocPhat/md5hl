package ra.md5.domain.user.dto.res.user;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDto<T> {
    private int code;
    private HttpStatus message;
    private T data;
}
