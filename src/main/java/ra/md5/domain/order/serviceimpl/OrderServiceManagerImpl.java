package ra.md5.domain.order.serviceimpl;
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
import ra.md5.domain.order.service.OrderServiceManager;
import ra.md5.domain.orderdetails.entity.OrderDetails;
import ra.md5.domain.orderdetails.repository.OrderDetailsRepository;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.product.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceManagerImpl implements OrderServiceManager {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    @Override
    public OrderChangeResponse changeStatusOrder(Integer orderId, OrderChangeDto request) {
        // Tìm đơn hàng theo orderId
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Không tìm thấy đơn hàng với ID: " + orderId));
        // Kiểm tra trạng thái mới hợp lệ
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Trạng thái không hợp lệ: " + request.getStatus());
        }
        // Không thể thay đổi trạng thái của đơn hàng đã bị hủy
        if (order.getStatus() == OrderStatus.CANCEL) {
            throw new OrderException("Không thể thay đổi trạng thái của đơn hàng đã bị hủy.");
        }
        // Không thể thay đổi trạng thái về mức thấp hơn trạng thái hiện tại
        if (newStatus.ordinal() < order.getStatus().ordinal()) {
            throw new OrderException("Không thể thay đổi trạng thái về mức thấp hơn trạng thái hiện tại.");
        }
        // Nếu trạng thái mới là CONFIRM, lưu thời gian xác nhận
        if (newStatus == OrderStatus.CONFIRM && order.getStatus() != OrderStatus.CONFIRM) {
            order.setConfirmedAt(LocalDateTime.now());
        }
        // Cập nhật ngày giao hàng nếu trạng thái là CONFIRM
        if (newStatus == OrderStatus.CONFIRM) {
            order.setReceivedAt(order.getConfirmedAt().plusDays(4)); // Dự đoán ngày giao hàng
        } else if (newStatus == OrderStatus.WAITING || newStatus == OrderStatus.CANCEL) {
            order.setReceivedAt(null); // Xóa ngày giao hàng nếu quay lại trạng thái WAITING hoặc CANCEL
        }
        // **Cập nhật số lượng sản phẩm đã bán khi trạng thái thành ACCEPTED**
        if (newStatus == OrderStatus.CONFIRM && order.getStatus() != OrderStatus.CONFIRM) {
            List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrder(order);
            for (OrderDetails details : orderDetailsList) {
                Product product = details.getProduct();
                int newSoldCount = (product.getSoldCount() != null ? product.getSoldCount() : 0) + details.getQuantity();
                product.setSoldCount(newSoldCount); // Cập nhật soldCount
                productRepository.save(product); // Lưu lại sản phẩm
            }
        }
        // Cập nhật trạng thái đơn hàng
        order.setStatus(newStatus);
        // Lưu thay đổi vào cơ sở dữ liệu
        orderRepository.save(order);
        // Chuẩn bị dữ liệu phản hồi
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
        // Tạo phản hồi
        OrderChangeResponse response = new OrderChangeResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(data);
        return response;
    }


}
