package ra.md5.domain.user.dto.req.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.address.entity.Address;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsDto {
    String username;
    String email;
    String firstName;
    String lastName;
    String avatar;
    String phone;
    List<Address> address;
}
