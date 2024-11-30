package ra.md5.domain.order.service;
import ra.md5.domain.order.dto.req.admin.InvoicesOverTimeRequest;
import ra.md5.domain.order.dto.req.admin.SalesRevenueRequest;
import ra.md5.domain.order.dto.res.admin.InvoicesOverTimeResponse;
import ra.md5.domain.order.dto.res.admin.OrderDetailsResponse;
import ra.md5.domain.order.dto.res.admin.OrderResponse;
import ra.md5.domain.order.dto.res.admin.SalesRevenueResponse;


public interface OrderServiceAdmin {
    OrderResponse getAllOrder();
    OrderResponse getOrderStatus(String orderStatus);
    OrderDetailsResponse orderDetails(Integer orderId);
    SalesRevenueResponse getSalesRevenueOverTime(SalesRevenueRequest request);
    InvoicesOverTimeResponse getInvoicesOverTime(InvoicesOverTimeRequest request);
}
