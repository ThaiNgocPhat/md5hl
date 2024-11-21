package ra.md5.controller.auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ra.md5.domain.user.dto.req.user.LoginDto;
import ra.md5.domain.user.dto.req.user.RegisterDto;
import ra.md5.domain.user.dto.res.user.LoginResponse;
import ra.md5.domain.user.dto.res.user.ResponseDto;
import ra.md5.domain.user.dto.res.user.UserResponseDto;
import ra.md5.domain.user.service.user.UserService;
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<ResponseDto<UserResponseDto>> register(@Valid @ModelAttribute RegisterDto request) {
        ResponseDto<UserResponseDto> response = userService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponse>> login(@Valid @RequestBody LoginDto request) {
        ResponseDto<LoginResponse> response = userService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}