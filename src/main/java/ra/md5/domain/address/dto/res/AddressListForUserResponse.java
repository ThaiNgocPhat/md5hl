package ra.md5.domain.address.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ra.md5.domain.address.dto.req.AddressListForUserResDto;
import ra.md5.domain.address.entity.Address;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressListForUserResponse {
    int code;
    HttpStatus message;
    AddressListForUserResDto data;
}
