package ra.md5.domain.user.serviceimpl.user;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.md5.common.security.jwt.JWTUtils;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.common.utils.UploadService;
import ra.md5.domain.address.entity.Address;
import ra.md5.domain.enums.EnumRole;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.product.exception.ImageUploadException;
import ra.md5.domain.product.exception.ProductNotFoundException;
import ra.md5.domain.product.repository.ProductRepository;
import ra.md5.domain.role.dto.RoleDto;
import ra.md5.domain.role.entity.Role;
import ra.md5.domain.role.repository.RoleRepository;
import ra.md5.domain.user.dto.req.user.*;
import ra.md5.domain.user.dto.res.user.*;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.DuplicateException;
import ra.md5.domain.user.exception.InvalidCredentialsException;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.exception.UserException;
import ra.md5.domain.user.repository.UserRepository;
import ra.md5.domain.user.service.user.UserService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final UploadService uploadService;
    private final JWTUtils jwtUtils;
    private final ModelMapper modelMapper;
    @Override
    public ResponseDto<UserResponseDto> register(RegisterDto request) {
        try {
            // Ánh xạ từ DTO sang User
            User user = modelMapper.map(request, User.class);
            // Xử lý upload ảnh
            String avatarUrl = "URL_ẢNH_MẶC_ĐỊNH"; // URL ảnh mặc định
            if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
                try {
                    avatarUrl = uploadService.uploadFileToDrive(request.getAvatar());
                } catch (IOException e) {
                    throw new RuntimeException("Lỗi khi tải ảnh lên", e);
                }
            }
            user.setAvatar(avatarUrl);
            // Gán vai trò mặc định
            Role role = roleRepository.findByRoleName(EnumRole.USER)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò USER"));
            user.setRoles(Collections.singletonList(role));
            // Mã hóa mật khẩu
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // Lưu người dùng vào cơ sở dữ liệu
            User savedUser = userRepository.save(user);
            // Tạo UserDetailsCustom từ savedUser
            UserDetailsCustom userDetailsCustom = new UserDetailsCustom(savedUser);
            // Tạo token cho người dùng
            String token = jwtUtils.generateAccessToken(userDetailsCustom); // Thay vì user, truyền userDetailsCustom
            // Ánh xạ từ User sang UserResponseDto
            UserResponseDto responseDto = modelMapper.map(savedUser, UserResponseDto.class);
            responseDto.setToken(token);
            // Chuyển đổi danh sách vai trò sang RoleDto
            List<RoleDto> roleDtos = savedUser.getRoles().stream()
                    .map(r -> new RoleDto(r.getRoleId(), r.getRoleName().name()))
                    .collect(Collectors.toList());
            responseDto.setAuthorities(roleDtos);
            // Tạo ResponseDto và trả về
            ResponseDto<UserResponseDto> response = new ResponseDto<>();
            response.setCode(201);
            response.setMessage(HttpStatus.CREATED);
            response.setData(responseDto);
            return response;
        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi duplicate, có thể throw DuplicateException hoặc trả về lỗi với mã 409
            throw new DuplicateException("Username đã tồn tại");
        }
    }



    @Override
    public ResponseDto<LoginResponse> login(LoginDto request) {
        // Kiểm tra người dùng có tồn tại hay không
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Không tìm thấy người dùng này"));
        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Mật khẩu không đúng");
        }
        // Tạo UserDetailsCustom từ User
        UserDetailsCustom userDetailsCustom = new UserDetailsCustom(user);
        // Tạo token nếu login thành công
        String token = jwtUtils.generateAccessToken(userDetailsCustom);  // Thay vì user, truyền userDetailsCustom
        // Chứa token trong LoginResponse
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        // Tạo phản hồi thành công
        ResponseDto<LoginResponse> responseDto = new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage(HttpStatus.OK);
        responseDto.setData(response);
        return responseDto;
    }

    @Override
    public UserDetailsResponse accountDetails(UserDetailsCustom userDetailsCustom) {
        // Tìm người dùng trong cơ sở dữ liệu
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Ánh xạ User sang UserDetailsDto
        UserDetailsDto userDetailsDto = new UserDetailsDto(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAvatar(),
                user.getAddresses()
        );
        // Tạo phản hồi
        UserDetailsResponse response = new UserDetailsResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(userDetailsDto);

        return response;
    }


    @Override
    public UserUpdateResponse updateUser(UserDetailsCustom userDetailsCustom, UserUpdateDto request) {
        //Kiểm tra điện thoại có trùng lặp hay không
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateException("Số điện thoại '" + request.getPhone() + "' đã được sử dụng.");
        }
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        // Xử lý avatar mới nếu có
        String imageUrl = user.getAvatar(); // Avatar hiện tại
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            try {
                // Upload ảnh lên drive và lấy URL
                imageUrl = uploadService.uploadFileToDrive(request.getAvatar());
            } catch (IOException e) {
                throw new ImageUploadException("Lỗi tải ảnh lên: " + e.getMessage());
            }
        }
        // Cập nhật avatar
        user.setAvatar(imageUrl);
        //Lưu vào database
        User users = userRepository.save(user);
        //Biến đổi từ User sang UserDetailsDto
        UserUpdateDetailsDto responseDto = modelMapper.map(users, UserUpdateDetailsDto.class);
        //Tạo phản hồi
        UserUpdateResponse response = new UserUpdateResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(responseDto);

        return response;
    }

    @Override
    public UserChangePasswordResponse changePassword(UserDetailsCustom userDetailsCustom, UserChangePasswordDto request) {
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        //Kiểm tra mật khẩu mới
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new UserException("Mật khẩu cũ không đúng");
        }
        //Mã hóa mật khẩu mới
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        //Lưu vào database
        userRepository.save(user);
        //Tạo phản hồi
        UserChangePasswordResponse response = new UserChangePasswordResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData("Đổi mật khẩu thành công");
        return response;
    }

}
