package ra.md5.domain.role.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.md5.domain.enums.EnumRole;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private EnumRole roleName = EnumRole.USER;
}
