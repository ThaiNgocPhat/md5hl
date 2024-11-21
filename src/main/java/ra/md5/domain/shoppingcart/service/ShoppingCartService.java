package ra.md5.domain.shoppingcart.service;
import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartAddDto;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartChangeQuantityDto;
import ra.md5.domain.shoppingcart.dto.req.ShoppingCartCheckoutDto;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartAddResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartListCartResponse;
import ra.md5.domain.shoppingcart.dto.res.ShoppingCartResponse;

public interface ShoppingCartService {
    ShoppingCartListCartResponse getListCart(UserDetailsCustom userDetailsCustom);
    ShoppingCartAddResponse addToCart(UserDetailsCustom userDetailsCustom, ShoppingCartAddDto request);
    ShoppingCartResponse changeOrderQuantity(UserDetailsCustom userDetailsCustom, Integer cartItem, ShoppingCartChangeQuantityDto request);
    ShoppingCartResponse deleteOneProduct(UserDetailsCustom userDetailsCustom, Integer cartItem);
    ShoppingCartResponse clearCart(UserDetailsCustom userDetailsCustom);
    ShoppingCartResponse checkout(UserDetailsCustom userDetailsCustom, ShoppingCartCheckoutDto request);
}
