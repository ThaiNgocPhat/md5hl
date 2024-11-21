package ra.md5.domain.wishlist.serviceimpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.product.exception.ProductNotFoundException;
import ra.md5.domain.product.repository.ProductRepository;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.user.repository.UserRepository;
import ra.md5.domain.wishlist.dto.req.WishListAddProductDto;
import ra.md5.domain.wishlist.dto.req.WishListDto;
import ra.md5.domain.wishlist.dto.res.WishListAddProductResponse;
import ra.md5.domain.wishlist.dto.res.WishListListResponse;
import ra.md5.domain.wishlist.entity.WishList;
import ra.md5.domain.wishlist.exception.WishListException;
import ra.md5.domain.wishlist.repository.WishListRepository;
import ra.md5.domain.wishlist.service.WishListService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public WishListAddProductResponse addWishList(UserDetailsCustom userDetailsCustom, WishListAddProductDto request) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        // Tạo mới một WishList nếu chưa có, nếu đã có thì sử dụng WishList hiện tại
        WishList wishList = wishListRepository.findByUser(user)
                .orElseGet(() -> {
                    WishList newWishList = new WishList();
                    newWishList.setUser(user);
                    return wishListRepository.save(newWishList); // Lưu và lấy wishlistId mới
                });

        // Kiểm tra sản phẩm có tồn tại không
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new WishListException("Sản phẩm không tồn tại"));

        // Kiểm tra sản phẩm có trong wishlist chưa
        if (wishList.getProducts().contains(product)) {
            throw new WishListException("Sản phẩm đã tồn tại trong wishlist");
        }

        // Thêm sản phẩm vào wishlist
        wishList.getProducts().add(product);
        wishListRepository.save(wishList); // Lưu wishlist với sản phẩm mới

        // Tạo phản hồi
        WishListDto productDetail = new WishListDto(
                wishList.getWishListId(), // wishlistId mới
                product.getSku(),
                product.getProductName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getStock(),
                product.getImage()
        );

        WishListAddProductResponse response = new WishListAddProductResponse();
        response.setCode(201);
        response.setMessage(HttpStatus.CREATED);
        response.setData(productDetail);
        return response;
    }

    @Override
    public WishListListResponse listWishList(UserDetailsCustom userDetailsCustom) {
        // Kiểm tra người dùng có tồn tại
        User user = userRepository.findByUsername(userDetailsCustom.getUsername())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        // Tìm wishlist của người dùng, trả về Optional<WishList>
        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new WishListException("Wishlist của người dùng không tồn tại"));

        // Chuyển danh sách sản phẩm trong wishlist thành danh sách DTO
        List<WishListDto> wishListDtos = wishList.getProducts().stream()
                .map(product -> {
                    WishListDto wishListDto = new WishListDto();
                    wishListDto.setWishListId(wishList.getWishListId()); // Gán id wishlist chung cho tất cả sản phẩm
                    wishListDto.setSku(product.getSku());
                    wishListDto.setProductName(product.getProductName());
                    wishListDto.setDescription(product.getDescription());
                    wishListDto.setUnitPrice(product.getUnitPrice());
                    wishListDto.setStock(product.getStock());
                    wishListDto.setImage(product.getImage() != null ? product.getImage() : null); // Kiểm tra ảnh

                    return wishListDto;
                })
                .collect(Collectors.toList());

        // Tạo đối tượng phản hồi
        WishListListResponse response = new WishListListResponse();
        response.setCode(200);
        response.setMessage(HttpStatus.OK);
        response.setData(wishListDtos); // Gán danh sách sản phẩm đã chuyển đổi vào data

        return response;
    }
}
