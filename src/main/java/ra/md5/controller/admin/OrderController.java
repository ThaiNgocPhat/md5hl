package ra.md5.controller.admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.md5.domain.order.dto.res.admin.OrderResponse;
import ra.md5.domain.order.service.admin.OrderServiceAdmin;

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
}
