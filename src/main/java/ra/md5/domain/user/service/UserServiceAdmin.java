package ra.md5.domain.user.service;

import ra.md5.domain.user.dto.req.admin.TopSpendingRequest;
import ra.md5.domain.user.dto.res.admin.*;

public interface UserServiceAdmin {
    UserListResponse getAllUser(int page, int size, String sort, String direction);
    UserListRoleResponse userListRole();
    UserSearchResponse searchUsername(String username);
    TopSpendingResponse getTopSpendingCustomers(TopSpendingRequest request);
    NewAccountsResponse getNewAccountsThisMonth();
}
