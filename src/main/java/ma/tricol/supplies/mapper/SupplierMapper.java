package ma.tricol.supplies.mapper;

import ma.tricol.supplies.dto.SupplierDTO;
import ma.tricol.supplies.model.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    
    SupplierDTO toDTO(Supplier supplier);
    
    Supplier toEntity(SupplierDTO dto);
}
