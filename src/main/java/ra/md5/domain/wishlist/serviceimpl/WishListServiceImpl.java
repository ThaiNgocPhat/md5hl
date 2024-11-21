package ra.md5.domain.wishlist.serviceimpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.product.exception.ProductNotFoundException;
import ra.md5.domain.product.repository.ProductRepository;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;
import ra.md5.domain.wishlist.dto.req.WishListProductDetailDto;
import ra.md5.domain.wishlist.dto.req.WishlistRequest;
import ra.md5.domain.wishlist.dto.res.WishListAddResponse;
import ra.md5.domain.wishlist.dto.res.WishListListResponse;
import ra.md5.domain.wishlist.entity.WishList;
import ra.md5.domain.wishlist.exception.WishListException;
import ra.md5.domain.wishlist.repository.WishListRepository;
import ra.md5.domain.wishlist.service.WishListService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public WishListAddResponse addWishList(UserDetailsCustom userDetailsCustom, WishlistRequest request) {
        // Kiểm tra sản phẩm có tồn tại hay không
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với id là " + request.getProductId()));

        // Lấy người dùng hiện tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        // Kiểm tra nếu wishlist của người dùng đã tồn tại
        WishList wishList = wishListRepository.findByUser(user)
                .orElseGet(() -> {
                    // Nếu không có wishlist thì tạo mới
                    WishList newWishList = new WishList();
                    newWishList.setUser(user);
                    return wishListRepository.save(newWishList);  // Lưu wishlist mới vào DB
                });

        // Kiểm tra nếu sản phẩm đã có trong wishlist của người dùng
        if (wishList.getProducts().contains(product)) {
            throw new WishListException("Sản phẩm đã có trong wishlist");
        }

        // Thêm sản phẩm vào wishlist
        wishList.getProducts().add(product);
        wishListRepository.save(wishList);  // Lưu wishlist vào DB

        // Tạo phản hồi với chi tiết sản phẩm vừa thêm
        WishListAddResponse response = new WishListAddResponse();
        response.setCode(201);
        response.setMessage(HttpStatus.CREATED);
        response.setData("Thêm sản phẩm vào wishlist thành công");

        return response;
    }



    // Phương thức lấy danh sách wishlist của người dùng
    public WishListListResponse getAllWishList(UserDetailsCustom userDetailsCustom) {
        // Lấy người dùng hiện tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        // Lấy wishlist của người dùng
        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new WishListException("Không tìm thấy wishlist của người dùng"));

        // Chuyển đổi danh sách sản phẩm trong wishlist thành danh sách DTO
        List<WishListProductDetailDto> wishListDtos = wishList.getProducts().stream()
                .map(product -> new WishListProductDetailDto(wishList.getWishlistId(),product.getSku(), product.getProductName(), product.getUnitPrice(), product.getDescription(), product.getStock(), product.getImage()))
                .collect(Collectors.toList());

        // Tạo phản hồi
        WishListListResponse response = new WishListListResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(wishListDtos);
        return response;
    }
}