package ra.md5.domain.user.service.admin;

import ra.md5.domain.user.dto.res.admin.UserListResponse;
import ra.md5.domain.user.dto.res.admin.UserListRoleResponse;
import ra.md5.domain.user.dto.res.admin.UserSearchResponse;

public interface UserServiceAdmin {
    UserListResponse getAllUser(int page, int size, String sort, String direction);
    UserListRoleResponse userListRole();
    UserSearchResponse searchUsername(String username);
}
