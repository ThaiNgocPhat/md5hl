package ra.md5.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int categoryId;

    @Column(name = "category_name", length = 100, nullable = false, unique = true)
    String categoryName;

    @Lob
    @Column(name = "description", length = 100, nullable = false)
    String description;

    @Column(name = "status")
    boolean status = true;

    @Column(name = "is_deleted", nullable = false)
    boolean isDeleted = false;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void update() {
        updatedAt = LocalDateTime.now();
    }
}
