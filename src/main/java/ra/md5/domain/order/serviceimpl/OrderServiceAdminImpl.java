package ra.md5.domain.order.serviceimpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.order.dto.req.admin.*;
import ra.md5.domain.order.dto.res.admin.InvoicesOverTimeResponse;
import ra.md5.domain.order.dto.res.admin.OrderDetailsResponse;
import ra.md5.domain.order.dto.res.admin.OrderResponse;
import ra.md5.domain.order.dto.res.admin.SalesRevenueResponse;
import ra.md5.domain.order.entity.Order;
import ra.md5.domain.order.exception.OrderException;
import ra.md5.domain.order.repository.OrderRepository;
import ra.md5.domain.order.service.OrderServiceAdmin;
import ra.md5.domain.user.exception.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        if (order.getUser() != null) {
            orderResDto.setUsername(order.getUser().getUsername());
        }
        // Tạo phản hồi và set data là orderResDto
        OrderDetailsResponse response = new OrderDetailsResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(orderResDto);
        return response;
    }
    @Override
    public SalesRevenueResponse getSalesRevenueOverTime(SalesRevenueRequest request) {
        // Kiểm tra đầu vào
        if (request.getFromDateTime() == null || request.getToDateTime() == null) {
            return new SalesRevenueResponse(400, HttpStatus.BAD_REQUEST, null);
        }

        // Lấy danh sách doanh thu từ HQL
        LocalDateTime from = request.getFromDateTime();
        LocalDateTime to = request.getToDateTime();

        List<DailyRevenue> results = orderRepository.findRevenueOverTime(from, to);
        if (results == null || results.isEmpty()) {
            return new SalesRevenueResponse(404, HttpStatus.NOT_FOUND, null);
        }

        // Tính tổng doanh thu
        BigDecimal totalRevenue = BigDecimal.ZERO;
        List<DailyRevenue> details = new ArrayList<>();
        for (DailyRevenue record : results) {
            totalRevenue = totalRevenue.add(record.getRevenue());
            details.add(new DailyRevenue(record.getDate(), record.getRevenue()));
        }

        // Tạo đối tượng TotalRevenueData
        TotalRevenueData totalRevenueData = new TotalRevenueData();
        totalRevenueData.setTotalRevenue(totalRevenue);
        totalRevenueData.setDetails(details);

        // Trả phản hồi
        SalesRevenueResponse response = new SalesRevenueResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(totalRevenueData);
        return response;
    }
    @Override
    public InvoicesOverTimeResponse getInvoicesOverTime(InvoicesOverTimeRequest request) {
        // Convert from String to LocalDateTime
        LocalDateTime fromDateTime = request.getFromDateTime();
        LocalDateTime toDateTime = request.getToDateTime();

        // Call the repository with the LocalDateTime parameters
        List<Object[]> result = orderRepository.findInvoicesOverTime(fromDateTime, toDateTime);

        List<InvoiceData> data = result.stream()
                .map(row -> {
                    InvoiceData invoice = new InvoiceData();
                    // Convert java.sql.Date to String
                    java.sql.Date date = (java.sql.Date) row[0];
                    invoice.setDate(date.toString());  // Convert to String
                    invoice.setInvoiceCount((long) row[1]);
                    return invoice;
                })
                .collect(Collectors.toList());

        InvoicesOverTimeResponse response = new InvoicesOverTimeResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(data);
        return response;
    }
}

