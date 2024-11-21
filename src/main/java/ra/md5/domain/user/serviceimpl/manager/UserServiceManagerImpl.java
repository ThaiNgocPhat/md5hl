package ra.md5.domain.user.serviceimpl.manager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.role.entity.Role;
import ra.md5.domain.role.exception.DuplicateRoleException;
import ra.md5.domain.role.exception.RoleNotFoundException;
import ra.md5.domain.role.repository.RoleRepository;
import ra.md5.domain.user.dto.res.manager.UserAddRoleResponse;
import ra.md5.domain.user.dto.res.manager.UserChangeStatusResponse;
import ra.md5.domain.user.dto.res.manager.UserDeleteRoleResponse;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;
import ra.md5.domain.user.service.manager.UserServiceManager;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceManagerImpl implements UserServiceManager {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public UserAddRoleResponse addRole(String userId, String roleId) {
        // Kiểm tra sự tồn tại của người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + userId));
        // Kiểm tra sự tồn tại của quyền
        Role role = roleRepository.findById(String.valueOf(roleId))
                .orElseThrow(() -> new RoleNotFoundException("Không tìm thấy quyền với ID: " + roleId));
        // Thêm quyền vào người dùng
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }else {
            throw new DuplicateRoleException("Người dùng đã có quyền này");
        }
        // Trả về phản hồi
        UserAddRoleResponse response = new UserAddRoleResponse();
        response.setCode(201);
        response.setMessage(HttpStatus.CREATED);
        response.setUsername(user.getUsername());
        response.setData(new ArrayList<>(user.getRoles()));
        return response;
    }

    @Override
    public UserDeleteRoleResponse deleteRole(String userId, String roleId) {
        // Kiểm tra sự tồn tại của người dùng
        User user = userRepository.findById(userId)
               .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + userId));
        // Kiểm tra sự tồn tại của quyền
        Role role = roleRepository.findById(String.valueOf(roleId))
               .orElseThrow(() -> new RoleNotFoundException("Không tìm thấy quyền với ID: " + roleId));
        // Xóa quyền người dùng
        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }else {
            throw new DuplicateRoleException("Người dùng không có quyền này");
        }
        // Trả về phản hồi
        UserDeleteRoleResponse response = new UserDeleteRoleResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setUsername(user.getUsername());
        response.setData(new ArrayList<>(user.getRoles()));
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
        //Trả về phản hồi
        UserChangeStatusResponse response = new UserChangeStatusResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setUsername(user.getUsername());
        response.setStatus(user.isStatus());
        return response;
    }
}
