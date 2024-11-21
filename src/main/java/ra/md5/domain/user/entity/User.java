package ra.md5.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.address.entity.Address;
import ra.md5.domain.role.entity.Role;
import ra.md5.domain.wishlist.entity.WishList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    String username;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "first_name", length = 50, nullable = false)
    String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    String lastName;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "avatar", nullable = false)
    String avatar;

    @Column(name = "phone", length = 11, nullable = false, unique = true)
    String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "status")
    boolean status = true;

    @Column(name = "is_deleted")
    boolean isDelete = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_role",
                joinColumns =  @JoinColumn(name = "user_id"),
    inverseJoinColumns =  @JoinColumn(name = "role_id"))
    List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishList> wishLists;

    @Column(name = "created_at",updatable = false)
    LocalDateTime createdAt;

    @Column(name = "update_at")
    LocalDateTime updateAt;

    public User(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.avatar = user.getAvatar();
        this.phone = user.getPhone();
        this.addresses = user.getAddresses();
        this.status = user.isStatus();
        this.isDelete = user.isDelete();
        this.roles = user.getRoles();
        this.createdAt = user.getCreatedAt();
        this.updateAt = user.getUpdateAt();
    }

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updateAt = LocalDateTime.now();
    }
}
