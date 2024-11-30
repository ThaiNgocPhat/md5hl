package ra.md5.domain.order.serviceimpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.order.dto.req.user.OrderDetailsDto;
import ra.md5.domain.order.dto.req.user.OrderDto;
import ra.md5.domain.order.dto.req.user.OrderSerialNumberDto;
import ra.md5.domain.order.dto.res.user.OrderCancelResponse;
import ra.md5.domain.order.dto.res.user.OrderGetBySerialNumberResponse;
import ra.md5.domain.order.dto.res.user.OrderResponse;
import ra.md5.domain.order.entity.Order;
import ra.md5.domain.order.exception.OrderIdNotFoundException;
import ra.md5.domain.order.exception.OrderStatusException;
import ra.md5.domain.order.exception.SerialNumberNotFound;
import ra.md5.domain.order.repository.OrderRepository;
import ra.md5.domain.order.service.OrderService;
import ra.md5.domain.orderdetails.entity.OrderDetails;
import ra.md5.domain.orderdetails.repository.OrderDetailsRepository;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderResponse getOrderHistory(UserDetailsCustom userDetailsCustom) {
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
        OrderResponse response = new OrderResponse();
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

        // Chuyển đổi đơn hàng sang DTO
        OrderSerialNumberDto orderDto = modelMapper.map(order, OrderSerialNumberDto.class);

        // Lấy chi tiết đơn hàng từ OrderDetails
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrder(order);

        // Ánh xạ OrderDetails thành DTO của sản phẩm đã mua
        List<OrderDetailsDto> orderDetailsDtos = orderDetailsList.stream()
                .map(orderDetails -> {
                    OrderDetailsDto detailsDto = modelMapper.map(orderDetails, OrderDetailsDto.class);
                    // Có thể bổ sung thông tin sản phẩm nếu cần
                    detailsDto.setProductName(orderDetails.getProduct().getProductName());
                    detailsDto.setUnitPrice(orderDetails.getUnitPrice());
                    detailsDto.setQuantity(orderDetails.getQuantity());
                    detailsDto.setTotalPrice(orderDetails.getTotalPrice());
                    return detailsDto;
                })
                .collect(Collectors.toList());

        // Thêm danh sách OrderDetails vào trong OrderDto
        orderDto.setOrderDetails(orderDetailsDtos);

        // Trả về phản hồi
        OrderGetBySerialNumberResponse response = new OrderGetBySerialNumberResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(orderDto);
        return response;
    }


    @Override
    public OrderResponse getOrderByStatus(UserDetailsCustom userDetailsCustom, String orderStatus) {
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
        OrderResponse response = new OrderResponse();
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
        //Nếu đơn hàng đã bị huỷ mà huỷ thêm một lần nữa
        if(order.getStatus().equals(OrderStatus.CANCEL)){
            return new OrderCancelResponse(200, HttpStatus.OK, "Đơn hàng đã bị huỷ rồi");
        }
        // Kiểm tra trạng thái của đơn hàng
        if (!order.getStatus().equals(OrderStatus.WAITING)) {
            return new OrderCancelResponse(400, HttpStatus.BAD_REQUEST, "Đơn hàng không thể hủy vì vì đã được chấp nhận");
        }
        order.setStatus(OrderStatus.CANCEL);
        //Lưu thay đổi
        orderRepository.save(order);
        return null;
    }
}
