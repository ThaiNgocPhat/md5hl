package ra.md5.domain.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.wishlist.entity.WishList;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Integer> {
    Optional<WishList> findByUser(User users);
}
