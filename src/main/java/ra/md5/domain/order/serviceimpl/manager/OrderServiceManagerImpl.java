package ra.md5.domain.order.serviceimpl.manager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.order.dto.req.manager.OrderChangeDto;
import ra.md5.domain.order.dto.req.manager.OrderChangeResDto;
import ra.md5.domain.order.dto.res.manager.OrderChangeResponse;
import ra.md5.domain.order.entity.Order;
import ra.md5.domain.order.exception.InvalidRequestException;
import ra.md5.domain.order.exception.OrderException;
import ra.md5.domain.order.repository.OrderRepository;
import ra.md5.domain.order.service.manager.OrderServiceManager;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceManagerImpl implements OrderServiceManager {
    private final OrderRepository orderRepository;
    @Override
    public OrderChangeResponse changeStatusOrder(Integer orderId, OrderChangeDto request) {
        // Tìm đơn hàng theo orderId
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Không tìm thấy đơn hàng với ID: " + orderId));

        // Kiểm tra trạng thái hợp lệ
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Trạng thái không hợp lệ: " + request.getStatus());
        }

        // Kiểm tra trạng thái hiện tại là CANCEL
        if (order.getStatus() == OrderStatus.CANCEL) {
            throw new OrderException("Không thể thay đổi trạng thái của đơn hàng đã bị hủy.");
        }

        // Kiểm tra nếu trạng thái mới thấp hơn trạng thái hiện tại
        if (newStatus.ordinal() < order.getStatus().ordinal()) {
            throw new OrderException("Không thể thay đổi trạng thái về mức thấp hơn trạng thái hiện tại.");
        }

        // Nếu trạng thái mới là CONFIRM, lưu lại thời điểm chấp nhận đơn hàng
        if (newStatus == OrderStatus.CONFIRM) {
            order.setConfirmedAt(LocalDateTime.now());
        }

        // Cập nhật ngày giao hàng dựa trên trạng thái CONFIRM
        if (order.getConfirmedAt() != null && newStatus != OrderStatus.WAITING) {
            order.setReceivedAt(order.getConfirmedAt().plusDays(4));
        } else {
            order.setReceivedAt(null); // Xóa ngày giao hàng nếu trạng thái quay về WAITING
        }

        // Cập nhật trạng thái đơn hàng
        order.setStatus(newStatus);

        // Lưu cập nhật vào cơ sở dữ liệu
        orderRepository.save(order);

        // Tạo phản hồi
        OrderChangeResDto data = new OrderChangeResDto(
                order.getOrderId(),
                order.getSerialNumber(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getReceiveName(),
                order.getReceivePhone(),
                order.getReceiveAddress(),
                order.getCreatedAt(),
                (order.getStatus() != OrderStatus.CANCEL) ? order.getReceivedAt() : null
        );
        //Trả phản hồi
        OrderChangeResponse response = new OrderChangeResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(data);
        return response;
    }

}
