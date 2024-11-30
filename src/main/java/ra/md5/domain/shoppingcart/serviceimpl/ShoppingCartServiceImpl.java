package ra.md5.domain.shoppingcart.serviceimpl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.order.entity.Order;
import ra.md5.domain.order.repository.OrderRepository;
import ra.md5.domain.orderdetails.entity.OrderDetails;
import ra.md5.domain.orderdetails.repository.OrderDetailsRepository;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.product.exception.ProductNotFoundException;
import ra.md5.domain.product.repository.ProductRepository;
import ra.md5.domain.shoppingcart.dto.req.*;
import ra.md5.domain.shoppingcart.dto.res.*;
import ra.md5.domain.shoppingcart.entity.ShoppingCart;
import ra.md5.domain.shoppingcart.exception.InsufficientStockException;
import ra.md5.domain.shoppingcart.exception.NotItemsException;
import ra.md5.domain.shoppingcart.exception.QuantityEnoughException;
import ra.md5.domain.shoppingcart.exception.ShoppingCartNotFoundException;
import ra.md5.domain.shoppingcart.repository.ShoppingCartRepository;
import ra.md5.domain.shoppingcart.service.ShoppingCartService;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Override
    public ShoppingCartListCartResponse getListCart(UserDetailsCustom userDetailsCustom) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        // Lấy danh sách giỏ hàng của người dùng
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUser(user);
        // Kiểm tra nếu giỏ hàng trống
        if (shoppingCarts.isEmpty()) {
            throw new NotFoundException("Giỏ hàng trống");
        }
        // Ánh xạ từ ShoppingCart entity sang ShoppingCartListCartDto
        List<ShoppingCartListCartDto> shoppingCartDtos = shoppingCarts.stream()
                .map(cart -> {
                    // Mappings for ShoppingCartListCartDto
                    ShoppingCartListCartDto dto = modelMapper.map(cart, ShoppingCartListCartDto.class);
                    // Manually set the productName from the Product entity
                    dto.setProductName(cart.getProduct().getProductName());
                    return dto;
                })
                .collect(Collectors.toList());
        // Tạo phản hồi
        ShoppingCartListCartResponse response = new ShoppingCartListCartResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(shoppingCartDtos);

        return response;
    }



    @Override
    public ShoppingCartAddResponse addToCart(UserDetailsCustom userDetailsCustom, ShoppingCartAddDto request) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        // Kiểm tra sản phẩm có tồn tại
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy mã sản phẩm: " + request.getProductId()));

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<ShoppingCart> existingCartOptional = shoppingCartRepository.findByUserAndProduct(user, product);
        ShoppingCart shoppingCart;

        if (existingCartOptional.isPresent()) {
            // Nếu sản phẩm đã có trong giỏ hàng, cộng thêm số lượng
            shoppingCart = existingCartOptional.get();
            shoppingCart.setOrderQuantity(shoppingCart.getOrderQuantity() + request.getOrderQuantity());
            shoppingCart.setTotalPrice(product.getUnitPrice().multiply(BigDecimal.valueOf(shoppingCart.getOrderQuantity())));
            // Lưu giỏ hàng đã cập nhật
            shoppingCartRepository.save(shoppingCart);
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới vào giỏ hàng
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCart.setProduct(product);
            shoppingCart.setOrderQuantity(request.getOrderQuantity());
            shoppingCart.setTotalPrice(product.getUnitPrice().multiply(BigDecimal.valueOf(request.getOrderQuantity())));
            // Lưu giỏ hàng mới
            shoppingCartRepository.save(shoppingCart);
        }

        // Tạo ShoppingCartAddResDto và ánh xạ dữ liệu
        ShoppingCartAddResDto responseDto = new ShoppingCartAddResDto();
        responseDto.setProductName(shoppingCart.getProduct().getProductName());
        responseDto.setOrderQuantity(shoppingCart.getOrderQuantity());
        responseDto.setUnitPrice(shoppingCart.getProduct().getUnitPrice());
        responseDto.setTotalPrice(shoppingCart.getTotalPrice());

        // Tạo phản hồi
        ShoppingCartAddResponse response = new ShoppingCartAddResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(responseDto);

        return response;
    }


    @Override
    public void changeOrderQuantity(UserDetailsCustom userDetailsCustom, Integer cartItem, ShoppingCartChangeQuantityDto request) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        //Kiểm tra sản phẩm có tồn tại trong giỏ hàng hay không
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartItem)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Không tìm thấy sản phẩm cần thay đổi số lượng"));
        //Cập nhật số lượng
        shoppingCart.setOrderQuantity(request.getOrderQuantity());
        //Cập nhật giá tiền
        shoppingCart.setTotalPrice(shoppingCart.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(shoppingCart.getOrderQuantity())));
        //Lưu giỏ hàng đã cập nhật
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void deleteOneProduct(UserDetailsCustom userDetailsCustom, Integer cartItem) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        //Kiểm tra sản phẩm có tồn tại trong giỏ hàng hay không
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartItem)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Không tìm thấy sản phẩm cần xoá"));
        //Xoá sản phẩm trong giỏ hàng
        shoppingCartRepository.delete(shoppingCart);
    }

    @Override
    @Transactional
    public void clearCart(UserDetailsCustom userDetailsCustom) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Xóa tất cả các sản phẩm trong giỏ hàng của người dùng
        shoppingCartRepository.deleteByUser(user);
        //Đặt lại giá trị id
        entityManager.createNativeQuery("ALTER TABLE shopping_cart AUTO_INCREMENT = 1").executeUpdate();
    }

    @Override
    public ShoppingCartOrderResponse checkout(UserDetailsCustom userDetailsCustom, ShoppingCartCheckoutDto request) {
        // Kiểm tra người dùng
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        // Lấy giỏ hàng của người dùng
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUser(user);
        if (shoppingCartList.isEmpty()) {
            throw new NotItemsException("Giỏ hàng trống, không thể thanh toán");
        }

        // Tính tổng giá trị đơn hàng
        BigDecimal totalPrice = shoppingCartList.stream()
                .map(ShoppingCart::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tạo mã đơn hàng (serial number)
        String serialNumber = UUID.randomUUID().toString();

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setSerialNumber(serialNumber);
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.WAITING);  // Trạng thái mặc định là WAITING
        order.setNote(request.getNote());
        order.setReceiveName(request.getReceiveName());
        order.setReceivePhone(request.getReceivePhone());
        order.setReceiveAddress(request.getReceiveAddress());
        order.setUser(user);

        // Lưu đơn hàng vào cơ sở dữ liệu
        orderRepository.save(order);

        // Tạo chi tiết đơn hàng và lưu
        List<OrderDetailsDto> orderDetailsDtos = new ArrayList<>();
        for (ShoppingCart item : shoppingCartList) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            orderDetails.setProduct(item.getProduct());
            orderDetails.setName(item.getProduct().getProductName());
            orderDetails.setUnitPrice(item.getProduct().getUnitPrice());
            orderDetails.setQuantity(item.getOrderQuantity());
            orderDetails.setTotalPrice(item.getTotalPrice());
            orderDetailsRepository.save(orderDetails);

            // Giảm số lượng sản phẩm trong kho
            Product product = item.getProduct();
            if (product.getStock() < item.getOrderQuantity()) {
                throw new QuantityEnoughException("Số lượng sản phẩm không đủ");
            }
            product.setStock(product.getStock() - item.getOrderQuantity());
            productRepository.save(product);

            // Thêm chi tiết đơn hàng vào danh sách trả về
            OrderDetailsDto detailsDto = new OrderDetailsDto(
                    item.getProduct().getProductName(),
                    item.getOrderQuantity(),
                    item.getProduct().getUnitPrice(),
                    item.getTotalPrice()
            );
            orderDetailsDtos.add(detailsDto);
        }

        // Xóa giỏ hàng
        shoppingCartRepository.deleteAll(shoppingCartList);

        // Tạo phản hồi đơn hàng
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setSerialNumber(serialNumber);
        orderResponseDto.setTotalPrice(totalPrice);
        orderResponseDto.setStatus(order.getStatus().name());
        orderResponseDto.setNote(order.getNote());
        orderResponseDto.setReceiveName(order.getReceiveName());
        orderResponseDto.setReceivePhone(order.getReceivePhone());
        orderResponseDto.setReceiveAddress(order.getReceiveAddress());
        orderResponseDto.setOrderDetails(orderDetailsDtos);

        // Trả về phản hồi
        ShoppingCartOrderResponse response = new ShoppingCartOrderResponse();
        response.setCode(201);
        response.setHttpStatus(HttpStatus.CREATED);
        response.setMessage("Đặt hàng thành công");
        response.setData(orderResponseDto);

        return response;
    }

}
