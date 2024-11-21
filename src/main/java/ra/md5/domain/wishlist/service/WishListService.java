package ra.md5.domain.wishlist.service;

import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.wishlist.dto.req.WishlistRequest;
import ra.md5.domain.wishlist.dto.res.WishListAddResponse;
import ra.md5.domain.wishlist.dto.res.WishListListResponse;

public interface WishListService {
    WishListAddResponse addWishList(UserDetailsCustom userDetailsCustom, WishlistRequest request);
    WishListListResponse getAllWishList(UserDetailsCustom userDetailsCustom);
}
