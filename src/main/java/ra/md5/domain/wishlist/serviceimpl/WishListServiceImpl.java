package ra.md5.domain.wishlist.serviceimpl;
import lombok.RequiredArgsConstructor;
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
import ra.md5.domain.wishlist.dto.res.WishListResponse;
import ra.md5.domain.wishlist.dto.res.WishListListResponse;
import ra.md5.domain.wishlist.entity.WishListItem;
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
    @Override
    public WishListResponse addWishList(UserDetailsCustom userDetailsCustom, WishlistRequest request) {
        //Lây người dùng
        User user = userRepository.findByUsername(userDetailsCustom.getUsername()).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        //Kiểm tra sản phẩm
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với id là " + request.getProductId()));
        //Kiểm tra có sản phẩm hay chưa
        if (wishListRepository.existsByUserAndProducts(user,product)) {
            throw new WishListException("Sản phẩm đã có trong danh sách yêu thích của bạn.");
        }
        // Thêm sản phẩm vào wishlist
        WishListItem wishListItem = new WishListItem();
        wishListItem.setUser(user);
        wishListItem.setProducts(product);
        wishListRepository.save(wishListItem);
        // Tạo phản hồi
        WishListResponse addResponse = new WishListResponse();
        addResponse.setCode(201);
        addResponse.setHttpStatus(HttpStatus.CREATED);
        addResponse.setMessage("Thêm mới thành công");
        return addResponse;
    }

    // Phương thức lấy danh sách wishlist của người dùng
    @Override
    public WishListListResponse getAllWishList(UserDetailsCustom userDetailsCustom) {
        // Lấy người dùng
        User user = userRepository.findByUsername(userDetailsCustom.getUsername()).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        // Lấy danh sách sản phẩm trong wishlist của người dùng
        List<WishListItem> wishListItems = wishListRepository.findAllByUser(user);
        // Tạo danh sách phản hồi
        List<WishListProductDetailDto> wishListDtos = wishListItems.stream()
               .map(wishListItem -> new WishListProductDetailDto(
                        wishListItem.getWishlistId(),
                        wishListItem.getProducts().getSku(),
                        wishListItem.getProducts().getProductName(),
                        wishListItem.getProducts().getUnitPrice(),
                        wishListItem.getProducts().getDescription(),
                        wishListItem.getProducts().getStock(),
                        wishListItem.getProducts().getImage()
                ))
                .collect(Collectors.toList());
        // Tạo phản hồi
        WishListListResponse response = new WishListListResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(wishListDtos);
        return response;
    }

    @Override
    public void deleteWishList(Integer wishListId) {
        // Kiểm tra id wishlist có tồn tại hay không
        WishListItem wishListItem = wishListRepository.findById(wishListId)
               .orElseThrow(() -> new NotFoundException("Không tìm thấy danh sách yêu thích với id là " + wishListId));
        // Xóa sản phẩm kh��i wishlist
        wishListRepository.delete(wishListItem);
    }
}