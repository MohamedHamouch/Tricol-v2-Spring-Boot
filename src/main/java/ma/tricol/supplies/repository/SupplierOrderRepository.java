package ma.tricol.supplies.repository;

import ma.tricol.supplies.model.SupplierOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierOrderRepository extends JpaRepository<SupplierOrder, Long> {
    Page<SupplierOrder> findBySupplierId(Long supplierId, Pageable pageable);
}
