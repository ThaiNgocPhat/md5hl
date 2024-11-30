package ra.md5.domain.orderdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.md5.domain.order.entity.Order;
import ra.md5.domain.orderdetails.entity.OrderDetails;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findByOrder(Order order);
}
