package ra.md5.domain.orderdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.md5.domain.orderdetails.entity.OrderDetails;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
}
