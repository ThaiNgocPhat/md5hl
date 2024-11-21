package ra.md5.domain.user.serviceimpl.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.md5.domain.role.repository.RoleRepository;
import ra.md5.domain.user.dto.req.admin.UserRoleInfo;
import ra.md5.domain.user.dto.res.admin.UserListResponse;
import ra.md5.domain.user.dto.res.admin.UserListRoleResponse;
import ra.md5.domain.user.dto.res.admin.UserSearchResponse;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;
import ra.md5.domain.user.service.admin.UserServiceAdmin;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceAdminImpl implements UserServiceAdmin {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public UserListResponse getAllUser(int page, int size, String sort, String direction) {
        //Sắp xếp
        Sort sortOrder = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        //Phân trang
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        //Lấy danh sách từ Repository
        Page<User> users = userRepository.findAll(pageable);
        //Lấy danh sách tùe page
        List<User> usersList = users.getContent();
        //Tạo response trả về
        UserListResponse response = new UserListResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(usersList);
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        return response;
    }

    @Override
    public UserListRoleResponse userListRole() {
        // Lấy tất cả người dùng từ cơ sở dữ liệu
        List<User> users = userRepository.findAll();
        // Tạo một danh sách để chứa thông tin người dùng và quyền của họ
        List<UserRoleInfo> userRoleInfoList = new ArrayList<>();
        // Lặp qua từng người dùng để lấy username và danh sách quyền
        for (User user : users) {
            UserRoleInfo userRoleInfo = new UserRoleInfo();
            userRoleInfo.setUsername(user.getUsername()); // Lấy tên người dùng
            userRoleInfo.setRoles(user.getRoles()); // Lấy danh sách quyền của người dùng
            userRoleInfoList.add(userRoleInfo); // Thêm vào danh sách
        }
        // Tạo và trả về response
        UserListRoleResponse response = new UserListRoleResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(userRoleInfoList); // Gán danh sách người dùng và quyền
        return response;
    }

    @Override
    public UserSearchResponse searchUsername(String username) {
        // Tìm người dùng theo tên
        List<User> user = userRepository.findByUsernameContainingIgnoreCase(username);
        // Kiểm tra nếu không có người dùng nào
        if (user.isEmpty()) {
            throw new NotFoundException("Tên người dùng không tồn tại");
        }
        // Tạo và trả về response
        UserSearchResponse response = new UserSearchResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(user);
        return response;
    }
}
