package ra.md5.domain.user.service.manager;
import ra.md5.domain.user.dto.res.manager.UserAddRoleResponse;
import ra.md5.domain.user.dto.res.manager.UserChangeStatusResponse;
import ra.md5.domain.user.dto.res.manager.UserDeleteRoleResponse;

public interface UserServiceManager {
    UserAddRoleResponse addRole(String userId, String roleId);
    UserDeleteRoleResponse deleteRole(String userId, String roleId);
    UserChangeStatusResponse changeStatus(String userId);
}
