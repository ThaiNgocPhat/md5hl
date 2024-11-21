package ra.md5.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ra.md5.domain.user.dto.res.admin.UserListResponse;
import ra.md5.domain.user.dto.res.admin.UserListRoleResponse;
import ra.md5.domain.user.dto.res.admin.UserSearchResponse;
import ra.md5.domain.user.service.admin.UserServiceAdmin;

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
}
