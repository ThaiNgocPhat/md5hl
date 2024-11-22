package ra.md5.domain.order.serviceimpl.admin;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.order.dto.req.admin.OrderResDto;
import ra.md5.domain.order.dto.res.admin.OrderDetailsResponse;
import ra.md5.domain.order.dto.res.admin.OrderResponse;
import ra.md5.domain.order.entity.Order;
import ra.md5.domain.order.exception.OrderException;
import ra.md5.domain.order.repository.OrderRepository;
import ra.md5.domain.order.service.admin.OrderServiceAdmin;
import ra.md5.domain.user.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceAdminImpl implements OrderServiceAdmin {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse getAllOrder() {
        // Lấy tất cả đơn hàng từ repository
        List<Order> orders = orderRepository.findAll();
        // Chuyển đổi các đối tượng Order thành OrderGetAllOrderDto
        List<OrderResDto> orderDtos = orders.stream()
                .map(order -> new OrderResDto(
                        order.getOrderId(),
                        order.getTotalPrice(),
                        order.getStatus().name(),
                        order.getNote(),
                        order.getReceiveName(),
                        order.getReceiveAddress(),
                        order.getReceivePhone(),
                        order.getCreatedAt(),
                        order.getUser() != null ? order.getUser().getUsername() : null
                ))
                .collect(Collectors.toList());
        //Tạo phản hồi
        OrderResponse response = new OrderResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(orderDtos);
        return response;
    }

    @Override
    public OrderResponse getOrderStatus(String orderStatus) {
        OrderStatus status;
        try{
            status = OrderStatus.valueOf(orderStatus);
        }catch (IllegalArgumentException e){
            throw new NotFoundException("Không tìm thấy trạng thái");
        }
        // Lấy danh sách đơn hàng theo trạng thái từ repository
        List<Order> orders = orderRepository.findByStatus(status);
        // Chuyển đ��i các đối tượng Order thành OrderGetAllOrderDto
        List<OrderResDto> orderDtos = orders.stream()
               .map(order -> new OrderResDto(
                        order.getOrderId(),
                        order.getTotalPrice(),
                        order.getStatus().name(),
                        order.getNote(),
                        order.getReceiveName(),
                        order.getReceiveAddress(),
                        order.getReceivePhone(),
                        order.getCreatedAt(),
                        order.getUser() != null? order.getUser().getUsername() : null
                ))
               .collect(Collectors.toList());
        // Tạo phản hồi
        OrderResponse response = new OrderResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(orderDtos);
        return response;
    }

    @Override
    public OrderDetailsResponse orderDetails(Integer orderId) {
        // Lấy đơn hàng theo ID từ repository
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Không tìm thấy đơn hàng"));

        // Chuyển đổi đối tượng Order thành OrderResDto
        OrderResDto orderResDto = modelMapper.map(order, OrderResDto.class);

        // Ensure username is populated from the User entity
        if (order.getUser() != null) {
            orderResDto.setUsername(order.getUser().getUsername());
        }

        // Tạo phản hồi và set data là orderResDto
        OrderDetailsResponse response = new OrderDetailsResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(orderResDto);  // Set OrderResDto instead of OrderDetailsResponse
        return response;
    }
}
