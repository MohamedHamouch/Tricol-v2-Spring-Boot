package ma.tricol.supplies.repository;

import ma.tricol.supplies.model.SupplierOrder;
import ma.tricol.supplies.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierOrderRepository extends JpaRepository<SupplierOrder, Long> {
    List<SupplierOrder> findBySupplierId(Long supplierId);
    
    @Query("SELECT o FROM SupplierOrder o WHERE " +
           "(:supplierId IS NULL OR o.supplier.id = :supplierId) AND " +
           "(:status IS NULL OR o.status = :status)")
    Page<SupplierOrder> findByFilters(@Param("supplierId") Long supplierId, 
                                       @Param("status") OrderStatus status, 
                                       Pageable pageable);
}
