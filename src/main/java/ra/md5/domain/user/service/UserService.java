package ra.md5.domain.user.service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.user.dto.req.user.*;
import ra.md5.domain.user.dto.res.user.*;
import ra.md5.domain.user.dto.res.user.LoginResponse;

public interface UserService {
    RegisterResponse register(RegisterDto request);
    LoginResponse login(LoginDto request);
    UserDetailsResponse accountDetails(UserDetailsCustom userDetailsCustom);
    UserUpdateResponse updateUser(UserDetailsCustom userDetailsCustom, UserUpdateDto request);
    void changePassword(UserDetailsCustom userDetailsCustom, UserChangePasswordDto request);
}
