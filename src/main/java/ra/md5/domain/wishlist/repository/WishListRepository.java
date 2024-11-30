package ra.md5.domain.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.user.entity.User;
import ra.md5.domain.wishlist.entity.WishListItem;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishListItem, Integer> {
    boolean existsByUserAndProducts( User user,Product product);
    List<WishListItem> findAllByUser(User user);
}
