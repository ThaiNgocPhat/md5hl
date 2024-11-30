package ra.md5.controller.admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md5.domain.order.dto.req.admin.InvoicesOverTimeRequest;
import ra.md5.domain.order.dto.req.admin.SalesRevenueRequest;
import ra.md5.domain.order.dto.res.admin.InvoicesOverTimeResponse;
import ra.md5.domain.order.dto.res.admin.OrderDetailsResponse;
import ra.md5.domain.order.dto.res.admin.OrderResponse;
import ra.md5.domain.order.dto.res.admin.SalesRevenueResponse;
import ra.md5.domain.order.service.OrderServiceAdmin;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/orders")
public class OrderController {
    private final OrderServiceAdmin orderServiceAdmin;
    @GetMapping
    public ResponseEntity<OrderResponse> getAllOrder(){
        OrderResponse orderList = orderServiceAdmin.getAllOrder();
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }
    @GetMapping("/{orderStatus}")
    public ResponseEntity<OrderResponse> getOrderStatus(@PathVariable String orderStatus){
        OrderResponse orderList = orderServiceAdmin.getOrderStatus(orderStatus);
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }
    @GetMapping("/orderId/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable Integer orderId){
        OrderDetailsResponse orderDetails = orderServiceAdmin.orderDetails(orderId);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    @GetMapping("/sales-revenue-over-time")
    public ResponseEntity<SalesRevenueResponse> getSalesRevenueOverTime(@RequestBody SalesRevenueRequest request) {
        SalesRevenueResponse response = orderServiceAdmin.getSalesRevenueOverTime(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/reports/invoices-over-time")
    public ResponseEntity<InvoicesOverTimeResponse> getInvoicesOverTime(@RequestBody InvoicesOverTimeRequest request) {
        InvoicesOverTimeResponse response = orderServiceAdmin.getInvoicesOverTime(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
