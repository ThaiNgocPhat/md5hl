package ra.md5.domain.order.service.user;

import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.order.dto.res.user.OrderCancelResponse;
import ra.md5.domain.order.dto.res.user.OrderGetBySerialNumberResponse;
import ra.md5.domain.order.dto.res.user.OrderGetStatusResponse;
import ra.md5.domain.order.dto.res.user.OrderHistoryResponse;

public interface OrderService {
    OrderHistoryResponse getOrderHistory(UserDetailsCustom userDetailsCustom);
    OrderGetBySerialNumberResponse getOrderBySerialNumber(UserDetailsCustom userDetailsCustom, String serialNumber);
    OrderGetStatusResponse getOrderByStatus(UserDetailsCustom userDetailsCustom, String orderStatus);
    OrderCancelResponse cancelOrder(UserDetailsCustom userDetailsCustom, Integer orderId);
}
