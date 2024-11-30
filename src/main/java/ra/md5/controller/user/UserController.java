package ra.md5.controller.user;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.address.dto.req.AddressAddDto;
import ra.md5.domain.address.dto.res.AddressResponse;
import ra.md5.domain.address.dto.res.AddressListForUserResponse;
import ra.md5.domain.address.service.AddressService;
import ra.md5.domain.order.dto.res.user.OrderGetBySerialNumberResponse;
import ra.md5.domain.order.dto.res.user.OrderResponse;
import ra.md5.domain.order.service.OrderService;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartAddDto;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartChangeQuantityDto;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartCheckoutDto;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartAddResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartListCartResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartOrderResponse;
import ra.md5.domain.shoppingcart.service.ShoppingCartService;
import ra.md5.domain.user.dto.req.user.UserChangePasswordDto;
import ra.md5.domain.user.dto.req.user.UserUpdateDto;
import ra.md5.domain.user.dto.res.user.UserDetailsResponse;
import ra.md5.domain.user.dto.res.user.UserUpdateResponse;
import ra.md5.domain.user.service.UserService;
import ra.md5.domain.wishlist.dto.req.WishlistRequest;
import ra.md5.domain.wishlist.dto.res.WishListResponse;
import ra.md5.domain.wishlist.dto.res.WishListListResponse;
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
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @Valid  @RequestBody UserChangePasswordDto request){
        userService.changePassword(userDetailsCustom, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //ADDRESS
    @PostMapping("/account/address")
    public ResponseEntity<AddressResponse> addAddress(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @Valid @RequestBody AddressAddDto request){
        AddressResponse addressAddResponse = addressService.addAddress(userDetailsCustom, request);
        return new ResponseEntity<>(addressAddResponse, HttpStatus.CREATED);
    }
    @DeleteMapping("/account/address/{addressId}")
    public ResponseEntity<?> deleteAddress(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer addressId){
        addressService.deleteOneAddress(userDetailsCustom, addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/account/address")
    public ResponseEntity<AddressListForUserResponse> listAddressForUser(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom){
        AddressListForUserResponse addresses = addressService.listAddressForUser(userDetailsCustom);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }
    @GetMapping("/account/address/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer addressId){
        AddressResponse addressGetByIdForUserResponse = addressService.getAddressById(userDetailsCustom, addressId);
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
    public ResponseEntity<?> changeOrderQuantity(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer cartItemId, @Valid @RequestBody ShoppingCartChangeQuantityDto request){
        shoppingCartService.changeOrderQuantity(userDetailsCustom, cartItemId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/cart/items/{cartItemId}")
    public ResponseEntity<String> deleteOneProduct(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer cartItemId){
        shoppingCartService.deleteOneProduct(userDetailsCustom, cartItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/cart/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom){
        shoppingCartService.clearCart(userDetailsCustom);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/checkout")
    public ResponseEntity<ShoppingCartOrderResponse> checkout(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom,
                                                              @RequestBody ShoppingCartCheckoutDto checkoutDto) {
        ShoppingCartOrderResponse response = shoppingCartService.checkout(userDetailsCustom, checkoutDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ORDER
    @GetMapping("/history")
    public ResponseEntity<OrderResponse> getHistory(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom){
        OrderResponse orderHistoryResponse = orderService.getOrderHistory(userDetailsCustom);
        return new ResponseEntity<>(orderHistoryResponse, HttpStatus.OK);
    }
    @GetMapping("/history/{serialNumber}")
    public ResponseEntity<OrderGetBySerialNumberResponse> getHistoryBySerialNumber(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable String serialNumber){
        OrderGetBySerialNumberResponse orderHistoryResponse = orderService.getOrderBySerialNumber(userDetailsCustom, serialNumber);
        return new ResponseEntity<>(orderHistoryResponse, HttpStatus.OK);
    }
    @GetMapping("/history/status/{orderStatus}")
    public ResponseEntity<OrderResponse> getHistoryByStatus(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable String orderStatus){
        OrderResponse orderGetStatusResponse = orderService.getOrderByStatus(userDetailsCustom, orderStatus);
        return new ResponseEntity<>(orderGetStatusResponse, HttpStatus.OK);
    }
    @PutMapping("/history/{orderId}/cancel")
    public ResponseEntity<?> orderChangeStatus(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Integer orderId){
        orderService.cancelOrder(userDetailsCustom, orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //WISHLIST
    @PostMapping("/wish-list")
    public ResponseEntity<WishListResponse> addToWishlist(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @RequestBody WishlistRequest request) {
        WishListResponse response = wishListService.addWishList(userDetailsCustom, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/wish-list")
    public ResponseEntity<WishListListResponse> getWishlist(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        WishListListResponse getAllWishList = wishListService.getAllWishList(userDetailsCustom);
        return new ResponseEntity<>(getAllWishList, HttpStatus.OK);
    }

    // Xóa sản phẩm khỏi danh sách yêu thích
    @DeleteMapping("/wish-list/{wishlistId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Integer wishlistId) {
        wishListService.deleteWishList(wishlistId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
