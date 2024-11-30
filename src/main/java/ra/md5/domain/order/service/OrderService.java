package ra.md5.domain.order.service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.order.dto.res.user.OrderCancelResponse;
import ra.md5.domain.order.dto.res.user.OrderGetBySerialNumberResponse;
import ra.md5.domain.order.dto.res.user.OrderResponse;

public interface OrderService {
    OrderResponse getOrderHistory(UserDetailsCustom userDetailsCustom);
    OrderGetBySerialNumberResponse getOrderBySerialNumber(UserDetailsCustom userDetailsCustom, String serialNumber);
    OrderResponse getOrderByStatus(UserDetailsCustom userDetailsCustom, String orderStatus);
    OrderCancelResponse cancelOrder(UserDetailsCustom userDetailsCustom, Integer orderId);
}
