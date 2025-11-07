package ma.tricol.supplies.mapper;

import ma.tricol.supplies.dto.ProductDTO;
import ma.tricol.supplies.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    ProductDTO toDTO(Product product);
    
    Product toEntity(ProductDTO dto);
}
