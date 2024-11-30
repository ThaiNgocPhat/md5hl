package ra.md5.domain.wishlist.service;

import ra.md5.common.security.principal.UserDetailsCustom;
import ra.md5.domain.wishlist.dto.req.WishlistRequest;
import ra.md5.domain.wishlist.dto.res.WishListResponse;
import ra.md5.domain.wishlist.dto.res.WishListListResponse;

public interface WishListService {
    WishListResponse addWishList(UserDetailsCustom userDetailsCustom, WishlistRequest request);
    WishListListResponse getAllWishList(UserDetailsCustom userDetailsCustom);
    void deleteWishList(Integer wishListId);
}
