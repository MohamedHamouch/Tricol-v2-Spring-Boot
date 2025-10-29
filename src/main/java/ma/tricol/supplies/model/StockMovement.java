package ma.tricol.supplies.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.tricol.supplies.model.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movement_date")
    private LocalDateTime movementDate;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "movement_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private SupplierOrder order;

    @PrePersist
    protected void onCreate() {
        if (movementDate == null) {
            movementDate = LocalDateTime.now();
        }
    }
}
