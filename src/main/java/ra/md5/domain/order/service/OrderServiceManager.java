package ra.md5.domain.order.service;

import ra.md5.domain.order.dto.req.manager.OrderChangeDto;
import ra.md5.domain.order.dto.res.manager.OrderChangeResponse;

public interface OrderServiceManager {
    OrderChangeResponse changeStatusOrder(Integer orderId, OrderChangeDto request);
}
