package ra.md5.domain.order.service.admin;
import ra.md5.domain.order.dto.res.admin.OrderResponse;


public interface OrderServiceAdmin {
    OrderResponse getAllOrder();
    OrderResponse getOrderStatus(String orderStatus);
}
