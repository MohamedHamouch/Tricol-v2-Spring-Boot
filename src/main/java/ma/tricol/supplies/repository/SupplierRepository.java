package ma.tricol.supplies.repository;

import ma.tricol.supplies.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    Optional<Supplier> findByIce(String ice);
    
    Optional<Supplier> findByCompany(String company);
}
