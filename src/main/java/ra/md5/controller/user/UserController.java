package ra.md5.controller.user;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.address.dto.req.AddressAddDto;
import ra.md5.domain.address.dto.res.AddressAddResponse;
import ra.md5.domain.address.dto.res.AddressDeleteOneResponse;
import ra.md5.domain.address.dto.res.AddressGetByIdForUserResponse;
import ra.md5.domain.address.dto.res.AddressListForUserResponse;
import ra.md5.domain.address.service.AddressService;
import ra.md5.domain.order.dto.res.user.OrderCancelResponse;
import ra.md5.domain.order.dto.res.user.OrderGetBySerialNumberResponse;
import ra.md5.domain.order.dto.res.user.OrderGetStatusResponse;
import ra.md5.domain.order.dto.res.user.OrderHistoryResponse;
import ra.md5.domain.order.service.user.OrderService;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartAddDto;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartChangeQuantityDto;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartCheckoutDto;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartAddResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartListCartResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartResponse;
import ra.md5.domain.shoppingcart.service.ShoppingCartService;
import ra.md5.domain.user.dto.req.user.UserChangePasswordDto;
import ra.md5.domain.user.dto.req.user.UserUpdateDto;
import ra.md5.domain.user.dto.res.user.UserChangePasswordResponse;
import ra.md5.domain.user.dto.res.user.UserDetailsResponse;
import ra.md5.domain.user.dto.res.user.UserUpdateResponse;
import ra.md5.domain.user.service.user.UserService;
import ra.md5.domain.wishlist.dto.req.WishlistRequest;
import ra.md5.domain.wishlist.dto.res.WishListAddResponse;
import ra.md5.domain.wishlist.dto.res.WishListListResponse;
import ra.md5.domain.wishlist.repository.WishListRepository;
import ra.md5.domain.wishlist.service.WishListService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final OrderService orderService;
    private final WishListService wishListService;
    private final AddressService addressService;
    private final ShoppingCartService shoppingCartService;

    //ACCOUNT
    @GetMapping("/account")
    public ResponseEntity<UserDetailsResponse> accountDetails(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom){
        UserDetailsResponse userDetailsResponse = userService.accountDetails(userDetailsCustom);
        return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);

    }
    @PutMapping("/account")
    public ResponseEntity<UserUpdateResponse> updateUser(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom,@Valid  @ModelAttribute UserUpdateDto request){
        UserUpdateResponse userDetailsResponse = userService.updateUser(userDetailsCustom, request);
        return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);
    }
    @PutMapping("/account/change-password")
    public ResponseEntity<UserChangePasswordResponse> changePassword(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @Valid  @RequestBody UserChangePasswordDto request){
        UserChangePasswordResponse userChangePasswordResponse = userService.changePassword(userDetailsCustom, request);
        return new ResponseEntity<>(userChangePasswordResponse, HttpStatus.OK);
    }

    //ADDRESS
    @PostMapping("/account/address")
    public ResponseEntity<AddressAddResponse> addAddress(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @Valid @RequestBody AddressAddDto request){
        AddressAddResponse addressAddResponse = addressService.addAddress(userDetailsCustom, request);
        return new ResponseEntity<>(addressAddResponse, HttpStatus.CREATED);
    }
    @DeleteMapping("/account/address/{addressId}")
    public ResponseEntity<AddressDeleteOneResponse> deleteAddress(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer addressId){
        AddressDeleteOneResponse addressDeleteOneResponse = addressService.deleteOneAddress(userDetailsCustom, addressId);
        return new ResponseEntity<>(addressDeleteOneResponse, HttpStatus.OK);
    }
    @GetMapping("/account/address")
    public ResponseEntity<AddressListForUserResponse> listAddressForUser(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom){
        AddressListForUserResponse addresses = addressService.listAddressForUser(userDetailsCustom);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }
    @GetMapping("/account/address/{addressId}")
    public ResponseEntity<AddressGetByIdForUserResponse> getAddressById(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer addressId){
        AddressGetByIdForUserResponse addressGetByIdForUserResponse = addressService.getAddressById(userDetailsCustom, addressId);
        return new ResponseEntity<>(addressGetByIdForUserResponse, HttpStatus.OK);
    }

    //SHOPPING CART
    @GetMapping("/cart/list")
    public ResponseEntity<ShoppingCartListCartResponse> listCart(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom){
        ShoppingCartListCartResponse shoppingCartListCartResponse = shoppingCartService.getListCart(userDetailsCustom);
        return new ResponseEntity<>(shoppingCartListCartResponse, HttpStatus.OK);
    }
    @PostMapping("/cart/add")
    public ResponseEntity<ShoppingCartAddResponse> addToCart(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @Valid @RequestBody ShoppingCartAddDto request){
        ShoppingCartAddResponse shoppingCartAddResponse = shoppingCartService.addToCart(userDetailsCustom, request);
        return new ResponseEntity<>(shoppingCartAddResponse, HttpStatus.CREATED);
    }
    @PutMapping("/cart/items/{cartItemId}")
    public ResponseEntity<ShoppingCartResponse> changeOrderQuantity(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer cartItemId, @Valid @RequestBody ShoppingCartChangeQuantityDto request){
        ShoppingCartResponse shoppingCartChangeQuantityResponse = shoppingCartService.changeOrderQuantity(userDetailsCustom, cartItemId, request);
        return new ResponseEntity<>(shoppingCartChangeQuantityResponse, HttpStatus.OK);
    }
    @DeleteMapping("/cart/items/{cartItemId}")
    public ResponseEntity<ShoppingCartResponse> deleteOneProduct(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer cartItemId){
        ShoppingCartResponse shoppingCartDeleteOneProductResponse = shoppingCartService.deleteOneProduct(userDetailsCustom, cartItemId);
        return new ResponseEntity<>(shoppingCartDeleteOneProductResponse, HttpStatus.OK);
    }
    @DeleteMapping("/cart/clear")
    public ResponseEntity<ShoppingCartResponse> clearCart(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom){
        ShoppingCartResponse shoppingCartClearCartResponse = shoppingCartService.clearCart(userDetailsCustom);
        return new ResponseEntity<>(shoppingCartClearCartResponse, HttpStatus.OK);
    }
    @PostMapping("/checkout")
    public ResponseEntity<ShoppingCartResponse> checkout(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom,
                                                  @RequestBody ShoppingCartCheckoutDto checkoutDto) {
        ShoppingCartResponse response = shoppingCartService.checkout(userDetailsCustom, checkoutDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ORDER
    @GetMapping("/history")
    public ResponseEntity<OrderHistoryResponse> getHistory(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom){
        OrderHistoryResponse orderHistoryResponse = orderService.getOrderHistory(userDetailsCustom);
        return new ResponseEntity<>(orderHistoryResponse, HttpStatus.OK);
    }
    @GetMapping("/history/{serialNumber}")
    public ResponseEntity<OrderGetBySerialNumberResponse> getHistoryBySerialNumber(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable String serialNumber){
        OrderGetBySerialNumberResponse orderHistoryResponse = orderService.getOrderBySerialNumber(userDetailsCustom, serialNumber);
        return new ResponseEntity<>(orderHistoryResponse, HttpStatus.OK);
    }
    @GetMapping("/history/status/{orderStatus}")
    public ResponseEntity<OrderGetStatusResponse> getHistoryByStatus(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable String orderStatus){
        OrderGetStatusResponse orderGetStatusResponse = orderService.getOrderByStatus(userDetailsCustom, orderStatus);
        return new ResponseEntity<>(orderGetStatusResponse, HttpStatus.OK);
    }
    @PutMapping("/history/{orderId}/cancel")
    public ResponseEntity<OrderCancelResponse> orderChangeStatus(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer orderId){
        OrderCancelResponse orderCancelResponse = orderService.cancelOrder(userDetailsCustom, orderId);
        return new ResponseEntity<>(orderCancelResponse, HttpStatus.OK);
    }

    //WISHLIST
    @PostMapping("/wish-list")
    public ResponseEntity<WishListAddResponse> addToWishlist(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @RequestBody WishlistRequest request) {
        WishListAddResponse response = wishListService.addWishList(userDetailsCustom, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/wish-list")
    public ResponseEntity<WishListListResponse> getWishlist(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        WishListListResponse getAllWishList = wishListService.getAllWishList(userDetailsCustom);
        return new ResponseEntity<>(getAllWishList, HttpStatus.OK);
    }
//
//    // Xóa sản phẩm khỏi danh sách yêu thích
//    @DeleteMapping("/wish-list/{wishlistId}")
//    public ResponseEntity<?> removeFromWishlist(@PathVariable String wishlistId) {
//        wishlistService.removeProductFromWishlist(wishlistId);
//        return ResponseEntity.ok("Product removed from wishlist");
//    }
}
