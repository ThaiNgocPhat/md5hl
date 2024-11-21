package ra.md5.domain.order.serviceimpl.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.order.dto.req.OrderDto;
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
        List<OrderDto> orderHistoryDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        // Trả phản hồi
        OrderHistoryResponse response = new OrderHistoryResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(orderHistoryDtos);
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
            return new OrderCancelResponse(400, HttpStatus.NOT_FOUND, "Đơn hàng không thể hủy vì không phải trạng thái chờ xác nhận");
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
