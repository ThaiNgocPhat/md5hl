package ra.md5.domain.shoppingcart.service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartAddDto;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartChangeQuantityDto;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartCheckoutDto;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartAddResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartListCartResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartOrderResponse;

public interface ShoppingCartService {
    ShoppingCartListCartResponse getListCart(UserDetailsCustom userDetailsCustom);
    ShoppingCartAddResponse addToCart(UserDetailsCustom userDetailsCustom, ShoppingCartAddDto request);
    void changeOrderQuantity(UserDetailsCustom userDetailsCustom, Integer cartItem, ShoppingCartChangeQuantityDto request);
    void deleteOneProduct(UserDetailsCustom userDetailsCustom, Integer cartItem);
    void clearCart(UserDetailsCustom userDetailsCustom);
    ShoppingCartOrderResponse checkout(UserDetailsCustom userDetailsCustom, ShoppingCartCheckoutDto request);
}
