package ma.tricol.supplies.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.tricol.supplies.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierOrderDTO {

    private Long id;

    private LocalDateTime orderDate;

    @NotNull(message = "Status is required")
    private OrderStatus status;

    private BigDecimal totalAmount;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    private String supplierCompany;

    private List<OrderProductDTO> orderProducts;
}
