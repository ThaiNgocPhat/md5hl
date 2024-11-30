package ra.md5.domain.user.service;
import ra.md5.domain.user.dto.res.manager.UserModificationResponse;
import ra.md5.domain.user.dto.res.manager.UserChangeStatusResponse;

public interface UserServiceManager {
    UserModificationResponse addRole(String userId, String roleId);
    UserModificationResponse deleteRole(String userId, String roleId);
    UserChangeStatusResponse changeStatus(String userId);
}
