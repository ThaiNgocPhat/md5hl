package ra.md5.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ra.md5.domain.enums.OrderStatus;
import ra.md5.domain.user.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderId;

    @Column(name = "serial_number", length = 100, nullable = false)
    String serialNumber;

    @Column(name = "total_price", nullable = false)
    BigDecimal totalPrice;

    @Column(name = "status")
    OrderStatus status;

    @Column(name = "note", length = 100, nullable = false)
    String note;

    @Column(name = "receive_name", length = 100, nullable = false)
    String receiveName;

    @Column(name = "receive_phone", length = 15, nullable = false)
    String receivePhone;

    @Column(name = "receive_address", length = 255, nullable = false)
    String receiveAddress;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
