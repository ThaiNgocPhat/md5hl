package ra.md5.domain.order.serviceimpl.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.order.dto.req.user.OrderDto;
import ra.md5.domain.order.dto.res.user.OrderCancelResponse;
import ra.md5.domain.order.dto.res.user.OrderGetBySerialNumberResponse;
import ra.md5.domain.order.dto.res.user.OrderGetStatusResponse;
import ra.md5.domain.order.dto.res.user.OrderHistoryResponse;
import ra.md5.domain.order.entity.Order;
import ra.md5.domain.order.exception.OrderIdNotFoundException;
import ra.md5.domain.order.exception.OrderStatusException;
import ra.md5.domain.order.exception.SerialNumberNotFound;
import ra.md5.domain.order.repository.OrderRepository;
import ra.md5.domain.order.service.user.OrderService;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderHistoryResponse getOrderHistory(UserDetailsCustom userDetailsCustom) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Lấy danh sách các đơn hàng của người dùng
        List<Order> orders = orderRepository.findByUser(user);
        // Kiểm tra nếu không có đơn hàng
        if (orders.isEmpty()) {
            throw new NotFoundException("Không có đơn hàng nào");
        }
        // Chuyển đổi danh sách Order sang danh sách OrderDto
        List<OrderDto> ordersDto = orders.stream()
                .map(order -> {
                    // Nếu trạng thái đơn hàng là CANCEL, không trả về receivedAt
                    if (order.getStatus() == OrderStatus.CANCEL) {
                        return new OrderDto(
                                order.getOrderId(),
                                order.getSerialNumber(),
                                order.getTotalPrice(),
                                order.getStatus(),
                                order.getReceiveName(),
                                order.getReceivePhone(),
                                order.getReceiveAddress(),
                                order.getCreatedAt(),
                                null // Trường receivedAt không xuất hiện khi trạng thái là CANCEL
                        );
                    } else {
                        return new OrderDto(
                                order.getOrderId(),
                                order.getSerialNumber(),
                                order.getTotalPrice(),
                                order.getStatus(),
                                order.getReceiveName(),
                                order.getReceivePhone(),
                                order.getReceiveAddress(),
                                order.getCreatedAt(),
                                order.getReceivedAt() // Trả về ngày nhận nếu trạng thái khác CANCEL
                        );
                    }
                })
                .collect(Collectors.toList());
        // Trả phản hồi
        OrderHistoryResponse response = new OrderHistoryResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(ordersDto);
        return response;
    }

    @Override
    public OrderGetBySerialNumberResponse getOrderBySerialNumber(UserDetailsCustom userDetailsCustom, String serialNumber) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
               .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Lấy đơn hàng theo số serial
        Order order = orderRepository.findBySerialNumberAndUser(serialNumber, user)
               .orElseThrow(() -> new SerialNumberNotFound("Không tìm thấy đơn hàng"));
        // Chuyển đ��i đơn hàng sang đơn hàng GetBySerialNumberResponse
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        // Trả phản hồi
        OrderGetBySerialNumberResponse response = new OrderGetBySerialNumberResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(orderDto);
        return response;
    }

    @Override
    public OrderGetStatusResponse getOrderByStatus(UserDetailsCustom userDetailsCustom, String orderStatus) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Kiểm tra trạng thái đơn hàng hợp lệ
        OrderStatus orderStatusEnum;
        try {
            orderStatusEnum = OrderStatus.valueOf(orderStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OrderStatusException("Trạng thái đơn hàng không hợp lệ");
        }
        // Nếu trạng thái yêu cầu là CANCEL, kiểm tra các điều kiện
        if (orderStatusEnum == OrderStatus.CANCEL) {
            // Lấy tất cả các đơn hàng của user
            List<Order> orders = orderRepository.findByUser(user);
            // Kiểm tra điều kiện không được hủy các đơn hàng đã chấp nhận
            boolean hasInvalidOrders = orders.stream()
                    .anyMatch(order ->
                            (order.getStatus() == OrderStatus.CONFIRM ||
                                    order.getStatus() == OrderStatus.DELIVERY ||
                                    order.getStatus() == OrderStatus.SUCCESS)
                    );
            if (hasInvalidOrders) {
                throw new OrderStatusException("Không thể hủy các đơn hàng đã được chấp nhận, đang giao, hoặc đã hoàn tất.");
            }
        }
        // Lấy danh sách đơn hàng theo trạng thái
        List<Order> orders = orderRepository.findByUserAndStatus(user, orderStatusEnum);
        // Kiểm tra nếu không có đơn hàng
        if (orders.isEmpty()) {
            throw new NotFoundException("Không tìm thấy đơn hàng nào với trạng thái: " + orderStatus);
        }
        // Chuyển đổi danh sách Order sang danh sách OrderDto
        List<OrderDto> orderDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        // Tạo phản hồi
        OrderGetStatusResponse response = new OrderGetStatusResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(orderDtos);
        return response;
    }


    @Override
    public OrderCancelResponse cancelOrder(UserDetailsCustom userDetailsCustom, Integer orderId) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Kiểm tra đơn hàng id có tồn tại hay không
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderIdNotFoundException("Không tìm thấy đơn hàng với id " + orderId));
        // Kiểm tra trạng thái của đơn hàng
        if (!order.getStatus().equals(OrderStatus.WAITING)) {
            return new OrderCancelResponse(200, HttpStatus.OK, "Đơn hàng không thể hủy vì vì đã được chấp nhận");
        }
        order.setStatus(OrderStatus.CANCEL);
        //Lưu thay đổi
        orderRepository.save(order);
        // Tạo phản hồi
        OrderCancelResponse response = new OrderCancelResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData("Hủy đơn hàng thành công");
        return response;
    }
}
