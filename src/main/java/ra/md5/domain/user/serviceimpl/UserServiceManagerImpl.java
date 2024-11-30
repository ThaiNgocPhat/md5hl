package ra.md5.domain.user.serviceimpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.role.dto.RoleDto;
import ra.md5.domain.role.entity.Role;
import ra.md5.domain.role.exception.DuplicateRoleException;
import ra.md5.domain.role.exception.RoleNotFoundException;
import ra.md5.domain.role.repository.RoleRepository;
import ra.md5.domain.user.dto.req.manager.UserChangeStatusDto;
import ra.md5.domain.user.dto.req.manager.UserModificationDto;
import ra.md5.domain.user.dto.res.manager.UserModificationResponse;
import ra.md5.domain.user.dto.res.manager.UserChangeStatusResponse;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;
import ra.md5.domain.user.service.UserServiceManager;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceManagerImpl implements UserServiceManager {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    @Override
    public UserModificationResponse addRole(String userId, String roleId) {
        // Kiểm tra sự tồn tại của người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + userId));

        // Kiểm tra sự tồn tại của quyền
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Không tìm thấy quyền với ID: " + roleId));

        // Thêm quyền vào người dùng
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        } else {
            throw new DuplicateRoleException("Người dùng đã có quyền này");
        }

        // Ánh xạ danh sách Role thành RoleDto bằng ModelMapper
        List<RoleDto> roleDtos = user.getRoles().stream()
                .map(r -> modelMapper.map(r, RoleDto.class))
                .collect(Collectors.toList());

        // Ánh xạ User sang UserModificationDto
        UserModificationDto userModificationDto = modelMapper.map(user, UserModificationDto.class);
        userModificationDto.setData(roleDtos); // Gán danh sách quyền đã ánh xạ

        // Tạo phản hồi
        UserModificationResponse response = new UserModificationResponse();
        response.setCode(201);
        response.setMessage(HttpStatus.CREATED);
        response.setData(userModificationDto);
        return response;
    }

    @Override
    public UserModificationResponse deleteRole(String userId, String roleId) {
        // Kiểm tra sự tồn tại của người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + userId));

        // Kiểm tra sự tồn tại của quyền
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Không tìm thấy quyền với ID: " + roleId));

        // Kiểm tra nếu người dùng chỉ có một quyền
        if (user.getRoles().size() <= 1) {
            throw new NotFoundException("Người dùng chỉ có một quyền, không thể xóa thêm.");
        }

        // Xóa quyền người dùng
        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            userRepository.save(user);
        } else {
            throw new DuplicateRoleException("Người dùng không có quyền này");
        }

        // Ánh xạ danh sách Role thành RoleDto bằng ModelMapper
        List<RoleDto> roleDtos = user.getRoles().stream()
                .map(r -> modelMapper.map(r, RoleDto.class))
                .collect(Collectors.toList());

        // Ánh xạ User sang UserModificationDto
        UserModificationDto userModificationDto = modelMapper.map(user, UserModificationDto.class);
        userModificationDto.setData(roleDtos); // Gán danh sách quyền đã ánh xạ

        // Tạo phản hồi
        UserModificationResponse response = new UserModificationResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(userModificationDto);
        return response;
    }



    @Override
    public UserChangeStatusResponse changeStatus(String userId) {
        //Kiểm tra sự tồn tại của người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ngừoi dùng"));
        //Thay đổi trạng thái ngừoi dùng
        user.setStatus(!user.isStatus());
        //Lưu thay đổi
        userRepository.save(user);
        //Ánh xạ Role thành RoleDto bằng ModelMapper
        List<RoleDto> roleDtos = user.getRoles().stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
        //Chuyển đổi Dto sang Entity
        UserChangeStatusDto userChangeStatusDto = modelMapper.map(user, UserChangeStatusDto.class);
        userChangeStatusDto.setRoles(roleDtos);
        //Trả về phản hồi
        UserChangeStatusResponse response = new UserChangeStatusResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(userChangeStatusDto);
        return response;
    }
}
