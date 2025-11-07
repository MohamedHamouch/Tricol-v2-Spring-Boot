package ma.tricol.supplies.mapper;

import ma.tricol.supplies.dto.OrderProductDTO;
import ma.tricol.supplies.model.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {
    
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderProductDTO toDTO(OrderProduct orderProduct);
    
    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "productId", target = "product.id")
    OrderProduct toEntity(OrderProductDTO dto);
}
