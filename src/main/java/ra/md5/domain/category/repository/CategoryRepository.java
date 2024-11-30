package ra.md5.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.category.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByCategoryName(String categoryName);
    @Query("UPDATE Category c SET c.isDeleted = true WHERE c.categoryId = :id")
    void softDeleteById(@Param("id") Integer id);

    @Query("SELECT c.categoryId, c.categoryName, SUM(od.totalPrice) FROM OrderDetails od JOIN od.product p JOIN p.category c GROUP BY c.categoryId, c.categoryName ORDER BY SUM(od.totalPrice) DESC")
    List<Object[]> findRevenueByCategory();
}
