package ra.md5.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.order.entity.Order;
import ra.md5.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
    Optional<Order> findBySerialNumberAndUser(String serialNumber, User user);
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status = :status")
    List<Order> findByUserAndStatus(@Param("user") User user, @Param("status") OrderStatus status);
    List<Order> findByStatus(OrderStatus status);
}
