package ra.md5.domain.order.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.order.dto.req.admin.DailyRevenue;
import ra.md5.domain.order.entity.Order;
import ra.md5.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
    Optional<Order> findBySerialNumberAndUser(String serialNumber, User user);
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status = :status")
    List<Order> findByUserAndStatus(@Param("user") User user, @Param("status") OrderStatus status);
    List<Order> findByStatus(OrderStatus status);
    @Query("SELECT new ra.md5.domain.order.dto.req.admin.DailyRevenue(o.createdAt, SUM(o.totalPrice)) " +
            "FROM Order o WHERE o.createdAt BETWEEN :from AND :to " +
            "GROUP BY o.createdAt")
    List<DailyRevenue> findRevenueOverTime(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT DATE(o.createdAt), COUNT(o.orderId) FROM Order o WHERE o.createdAt BETWEEN :from AND :to GROUP BY DATE(o.createdAt)")
    List<Object[]> findInvoicesOverTime(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
