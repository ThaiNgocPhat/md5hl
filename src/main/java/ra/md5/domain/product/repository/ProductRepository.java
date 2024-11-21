package ra.md5.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.product.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsProductBySku(String sku);
    boolean existsProductByProductName(String productName);
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Integer categoryId);
    List<Product> findTop5ByStatusTrueOrderByCreatedAtDesc();
}
