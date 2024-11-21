package ra.md5.common.security.principal;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ra.md5.domain.user.entity.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class UserDetailsCustom implements UserDetails {
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String avatar;
    private String phone;
    private String address;
    private boolean status;
    private boolean delete;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsCustom(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.avatar = user.getAvatar();
        this.phone = user.getPhone();
        this.status = user.isStatus();
        this.delete = user.isDelete();
        // Chuyển đổi các role thành GrantedAuthority
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
