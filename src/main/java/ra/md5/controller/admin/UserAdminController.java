package ra.md5.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md5.domain.user.dto.req.admin.TopSpendingRequest;
import ra.md5.domain.user.dto.res.admin.*;
import ra.md5.domain.user.service.UserServiceAdmin;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class UserAdminController {
    private final UserServiceAdmin userServiceAdmin;
    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers(@RequestParam int page,
                                                        @RequestParam int size,
                                                        @RequestParam String sort,
                                                        @RequestParam String direction) {
        UserListResponse users = userServiceAdmin.getAllUser(page, size, sort, direction);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/roles")
    public ResponseEntity<UserListRoleResponse> getAllRoles(){
        UserListRoleResponse getAllRole = userServiceAdmin.userListRole();
        return new ResponseEntity<>(getAllRole, HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<UserSearchResponse> searchByUsername(@RequestParam String username){
        UserSearchResponse searchUsername = userServiceAdmin.searchUsername(username);
        return new ResponseEntity<>(searchUsername, HttpStatus.OK);
    }
    @GetMapping("/reports/top-spending-customers")
    public ResponseEntity<TopSpendingResponse> getTopSpendingCustomers(@RequestBody TopSpendingRequest request) {
        TopSpendingResponse response = userServiceAdmin.getTopSpendingCustomers(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/reports/new-accounts-this-month")
    public ResponseEntity<NewAccountsResponse> getNewAccountsThisMonth() {
        NewAccountsResponse response = userServiceAdmin.getNewAccountsThisMonth();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
