package ra.md5.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.category.entity.Category;
import ra.md5.domain.wishlist.entity.WishList;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    Integer productId;

    @Column(name = "sku", length = 15, nullable = false, unique = true)
    String sku;

    @Column(name = "product_name", length = 100, nullable = false, unique = true)
    String productName;

    @Lob
    @Column(name = "description")
    String description;

    @Column(name = "unit_price", nullable = false)
    BigDecimal unitPrice;

    @Column(name = "stock", nullable = false)
    Integer stock;

    @Column(name = "image")
    String image;

    @Column(name = "status")
    boolean status = true;

    @Column(name = "is_delete")
    Boolean isDeleted = false;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne
    @JsonIgnoreProperties({"status", "description", "createdAt", "updatedAt"})
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
