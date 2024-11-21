package ra.md5.domain.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.md5.domain.product.entity.Product;
import ra.md5.domain.shoppingcart.entity.ShoppingCart;
import ra.md5.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    List<ShoppingCart> findByUser(User user);
    Optional<ShoppingCart> findByUserAndProduct(User user, Product product);
    void deleteByUser(User user);
}
