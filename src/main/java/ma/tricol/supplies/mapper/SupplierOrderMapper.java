package ma.tricol.supplies.mapper;

import ma.tricol.supplies.dto.SupplierOrderDTO;
import ma.tricol.supplies.model.SupplierOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderProductMapper.class})
public interface SupplierOrderMapper {
    
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.company", target = "supplierCompany")
    SupplierOrderDTO toDTO(SupplierOrder order);
    
    @Mapping(source = "supplierId", target = "supplier.id")
    @Mapping(target = "orderProducts", ignore = true)
    SupplierOrder toEntity(SupplierOrderDTO dto);
}
