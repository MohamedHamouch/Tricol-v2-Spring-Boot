package ma.tricol.supplies.mapper;

import ma.tricol.supplies.dto.StockMovementDTO;
import ma.tricol.supplies.model.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {
    
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "order.id", target = "orderId")
    StockMovementDTO toDTO(StockMovement movement);
    
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "orderId", target = "order.id")
    StockMovement toEntity(StockMovementDTO dto);
}
