package ra.md5.domain.shoppingcart.serviceimpl;

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
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartAddResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartListCartResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartResponse;
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

    @Override
    public ShoppingCartListCartResponse getListCart(UserDetailsCustom userDetailsCustom) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        // Lấy danh sách giỏ hàng của người dùng (giả sử có Cart entity)
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUser(user);

        // Ánh xạ từ ShoppingCart entity sang ShoppingCartListCartDto
        List<ShoppingCartListCartDto> shoppingCartDtos = shoppingCarts.stream()
                .map(cart -> modelMapper.map(cart, ShoppingCartListCartDto.class))
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
            shoppingCart = existingCartOptional.get(); // Lấy giỏ hàng từ Optional
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
        // Ánh xạ ShoppingCart sang ShoppingCartAddResDto
        ShoppingCartAddResDto responseDto = modelMapper.map(shoppingCart, ShoppingCartAddResDto.class);
        // Tạo phản hồi
        ShoppingCartAddResponse response = new ShoppingCartAddResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(responseDto);
        return response;
    }

    @Override
    public ShoppingCartResponse changeOrderQuantity(UserDetailsCustom userDetailsCustom, Integer cartItem, ShoppingCartChangeQuantityDto request) {
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
        //Tạo phản hồi
        ShoppingCartResponse response = new ShoppingCartResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData("Cập nhật số lượng thành công");
        return response;
    }

    @Override
    public ShoppingCartResponse deleteOneProduct(UserDetailsCustom userDetailsCustom, Integer cartItem) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        //Kiểm tra sản phẩm có tồn tại trong giỏ hàng hay không
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartItem)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Không tìm thấy sản phẩm cần xoá"));
        //Xoá sản phẩm trong giỏ hàng
        shoppingCartRepository.delete(shoppingCart);
        //Tạo phản hồi
        ShoppingCartResponse response = new ShoppingCartResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData("Xoá sản phẩm thành công");
        return response;

    }

    @Override
    @Transactional
    public ShoppingCartResponse clearCart(UserDetailsCustom userDetailsCustom) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Xóa tất cả các sản phẩm trong giỏ hàng của người dùng
        shoppingCartRepository.deleteByUser(user);
        // Tạo phản hồi
        ShoppingCartResponse response = new ShoppingCartResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData("Xoá toàn bộ sản phẩm trong giỏ hàng thành công");
        return response;
    }

    @Transactional
    @Override
    public ShoppingCartResponse checkout(UserDetailsCustom userDetailsCustom, ShoppingCartCheckoutDto request) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        // Lấy giỏ hàng của người dùng
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUser(user);
        if (shoppingCartList.isEmpty()) {
            throw new NotItemsException("Giỏ hàng trống, không thể thanh toán");
        }

        // Tính toán tổng giá trị đơn hàng
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ShoppingCart item : shoppingCartList) {
            totalPrice = totalPrice.add(item.getTotalPrice());
        }

        // Tạo mã đơn hàng (serial number)
        String serialNumber = UUID.randomUUID().toString();

        // Tạo đơn hàng mới từ thông tin nhập vào
        Order order = new Order();
        order.setSerialNumber(serialNumber);
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.WAITING);  // Trạng thái đơn hàng khi mới tạo
        order.setNote(request.getNote());  // Lấy ghi chú từ người dùng
        order.setReceiveName(request.getReceiveName());  // Lấy tên người nhận từ người dùng
        order.setReceivePhone(request.getReceivePhone());  // Lấy số điện thoại người nhận từ người dùng
        order.setReceiveAddress(request.getReceiveAddress());  // Lấy địa chỉ người nhận từ người dùng
        order.setUser(user);

        // Lưu đơn hàng vào cơ sở dữ liệu
        orderRepository.save(order);

        // Ánh xạ giỏ hàng sang OrderDetails và lưu từng chi tiết đơn hàng
        for (ShoppingCart item : shoppingCartList) {
            // Tạo đối tượng OrderDetails
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            orderDetails.setProduct(item.getProduct());
            orderDetails.setName(item.getProduct().getProductName()); // Lấy tên sản phẩm
            orderDetails.setUnitPrice(item.getProduct().getUnitPrice());
            orderDetails.setQuantity(item.getOrderQuantity());
            orderDetails.setTotalPrice(item.getTotalPrice());

            // Lưu chi tiết đơn hàng
            orderDetailsRepository.save(orderDetails);

            // Cập nhật số lượng sản phẩm trong kho
            Product product = item.getProduct();
            if (product.getStock() < item.getOrderQuantity()) {
                throw new QuantityEnoughException("Số lượng sản phẩm không đủ");
            }
            product.setStock(product.getStock() - item.getOrderQuantity());
            productRepository.save(product);
        }

        // Xóa giỏ hàng sau khi thanh toán
        shoppingCartRepository.deleteAll(shoppingCartList);

        // Trả về thông tin đơn hàng đã được tạo
        ShoppingCartResponse response = new ShoppingCartResponse();
        response.setCode(201);
        response.setMessage(HttpStatus.CREATED);
        response.setData("Đặt hàng thành công");
        return response;
    }
}
