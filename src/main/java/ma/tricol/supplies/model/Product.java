package ma.tricol.supplies.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    private String category;

    @Column(name = "current_stock")
    private Integer currentStock = 0;

    @Column(name = "cump")
    private BigDecimal cump = BigDecimal.ZERO;
}
