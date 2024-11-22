package ra.md5.domain.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.wishlist.entity.WishList;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Integer> {
    Optional<WishList> findByUser(User users);
    @Query("SELECT w FROM WishList w WHERE w.user = :user")
    Optional<WishList> findFreshWishListByUser(@Param("user") User user);

}
