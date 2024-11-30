package ra.md5.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsUserByUsername(String username);
    boolean existsByPhone(String phone);
    Optional<User> findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);
    @Query("SELECT c.userId, c.username, SUM(o.totalPrice) FROM Order o JOIN o.user c WHERE o.createdAt BETWEEN :from AND :to GROUP BY c.userId, c.username ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> findTopSpenders(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
    @Query("SELECT u FROM User u WHERE u.createdAt >= :startOfMonth")
    List<User> findNewAccounts(@Param("startOfMonth") LocalDateTime startOfMonth);
}
