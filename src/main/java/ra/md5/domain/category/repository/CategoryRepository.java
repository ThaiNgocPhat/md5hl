package ra.md5.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.md5.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByCategoryName(String categoryName);
    @Query("UPDATE Category c SET c.isDeleted = true WHERE c.categoryId = :id")
    void softDeleteById(@Param("id") Integer id);
}
