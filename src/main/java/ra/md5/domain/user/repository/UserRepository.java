package ra.md5.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.md5.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsUserByUsername(String username);
    boolean existsByPhone(String phone);
    Optional<User> findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);
}
