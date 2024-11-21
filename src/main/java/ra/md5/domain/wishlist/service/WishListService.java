package ra.md5.domain.wishlist.service;

import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.wishlist.dto.req.WishListAddProductDto;
import ra.md5.domain.wishlist.dto.res.WishListAddProductResponse;
import ra.md5.domain.wishlist.dto.res.WishListListResponse;

public interface WishListService {
    WishListAddProductResponse addWishList(UserDetailsCustom userDetailsCustom, WishListAddProductDto request);
    WishListListResponse listWishList(UserDetailsCustom userDetailsCustom);
}
