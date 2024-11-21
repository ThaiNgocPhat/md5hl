package ra.md5.domain.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.md5.domain.enums.EnumRole;
import ra.md5.domain.role.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(EnumRole roleEnum);
}
