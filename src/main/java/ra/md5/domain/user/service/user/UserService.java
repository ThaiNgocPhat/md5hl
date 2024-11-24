package ra.md5.domain.user.service.user;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.user.dto.req.user.*;
import ra.md5.domain.user.dto.res.user.*;

public interface UserService {
    ResponseDto<UserResponseDto> register(RegisterDto request);
    ResponseDto<LoginResponse> login(LoginDto request);
    UserDetailsResponse accountDetails(UserDetailsCustom userDetailsCustom);
    UserUpdateResponse updateUser(UserDetailsCustom userDetailsCustom, UserUpdateDto request);
    UserChangePasswordResponse changePassword(UserDetailsCustom userDetailsCustom, UserChangePasswordDto request);
}
