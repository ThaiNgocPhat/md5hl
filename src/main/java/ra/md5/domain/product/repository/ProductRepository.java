package ra.md5.domain.product.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.product.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsProductBySku(String sku);
    boolean existsProductByProductName(String productName);
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Integer categoryId);
    List<Product> findTop5ByStatusTrueOrderByCreatedAtDesc();
    List<Product> findTop5BySoldCountGreaterThanOrderBySoldCountDesc(int soldCount);
    @Query("SELECT p.productId, p.productName, SUM(od.quantity) FROM OrderDetails od JOIN od.product p WHERE od.order.createdAt BETWEEN :from AND :to GROUP BY p.productId, p.productName ORDER BY SUM(od.quantity) DESC")
    List<Object[]> findBestSellers(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT p.productId, p.productName, COUNT(w) " +
            "FROM Product p JOIN WishListItem w ON p.productId = w.products.productId " +
            "WHERE w.user.createdAt BETWEEN :from AND :to " +
            "GROUP BY p.productId, p.productName " +
            "ORDER BY COUNT(w) DESC")
    List<Object[]> findMostWishlistProducts(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
