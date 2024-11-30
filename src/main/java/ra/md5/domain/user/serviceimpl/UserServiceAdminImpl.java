package ra.md5.domain.user.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.role.dto.RoleDto;
import ra.md5.domain.user.dto.req.admin.CustomerSpendingData;
import ra.md5.domain.user.dto.req.admin.TopSpendingRequest;
import ra.md5.domain.user.dto.req.admin.UserRoleInfo;
import ra.md5.domain.user.dto.req.user.UserDto;
import ra.md5.domain.user.dto.res.admin.*;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;
import ra.md5.domain.user.service.UserServiceAdmin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceAdminImpl implements UserServiceAdmin {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public UserListResponse getAllUser(int page, int size, String sort, String direction) {
        // Sắp xếp
        Sort sortOrder = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        // Phân trang
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        // Lấy danh sách người dùng từ repository
        Page<User> usersPage = userRepository.findAll(pageable);
        // Lấy danh sách người dùng từ page
        List<User> usersList = usersPage.getContent();

        // Ánh xạ từ User sang UserDto và loại bỏ mật khẩu, chỉ lấy roleName
        List<UserDto> userDtos = usersList.stream().map(user -> {
            // Ánh xạ User sang UserDto
            UserDto userDto = modelMapper.map(user, UserDto.class);

            // Ánh xạ các Role thành RoleDto và lấy roleName (chuyển EnumRole thành String)
            List<RoleDto> roleDtos = user.getRoles().stream()
                    .map(role -> {
                        RoleDto roleDto = new RoleDto();
                        roleDto.setRoleName(role.getRoleName().name());
                        return roleDto;
                    })
                    .collect(Collectors.toList());

            userDto.setRoles(roleDtos);  // Gán danh sách RoleDto vào UserDto
            return userDto;
        }).collect(Collectors.toList());

        // Tạo phản hồi
        UserListResponse response = new UserListResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(userDtos);
        response.setTotalElements(usersPage.getTotalElements());
        response.setTotalPages(usersPage.getTotalPages());
        response.setPage(page);
        response.setSize(size);
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
            // Lấy thông tin người dùng
            userRoleInfo.setUsername(user.getUsername());
            // Chuyển đổi roles thành List<RoleDto>
            List<RoleDto> roleDtos = user.getRoles().stream()
                    .map(role -> {
                        RoleDto roleDto = new RoleDto();
                        roleDto.setRoleName(role.getRoleName().name());  // Chuyển EnumRole thành String
                        return roleDto;
                    })
                    .collect(Collectors.toList());
            // Gán danh sách RoleDto vào UserRoleInfo
            userRoleInfo.setRoles(roleDtos);
            // Thêm vào danh sách
            userRoleInfoList.add(userRoleInfo);
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

    @Override
    public TopSpendingResponse getTopSpendingCustomers(TopSpendingRequest request) {
        // Convert the String from and to to LocalDateTime
        LocalDateTime fromDateTime = request.getFromDateTime();
        LocalDateTime toDateTime = request.getToDateTime();

        // Call the repository with the LocalDateTime parameters
        List<Object[]> result = userRepository.findTopSpenders(fromDateTime, toDateTime);

        List<CustomerSpendingData> data = result.stream()
                .map(row -> {
                    CustomerSpendingData spending = new CustomerSpendingData();
                    spending.setCustomerId((String) row[0]);
                    spending.setCustomerName((String) row[1]);

                    // Convert BigDecimal to double
                    BigDecimal totalSpent = (BigDecimal) row[2];
                    spending.setTotalSpent(totalSpent.doubleValue());

                    return spending;
                })
                .collect(Collectors.toList());

        TopSpendingResponse response = new TopSpendingResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(data);
        return response;
    }


    @Override
    public NewAccountsResponse getNewAccountsThisMonth() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        List<User> newUsers = userRepository.findNewAccounts(startDateTime);

        List<UserDto> data = newUsers.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        NewAccountsResponse response = new NewAccountsResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(data);
        return response;
    }


}
