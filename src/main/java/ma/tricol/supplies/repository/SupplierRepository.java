package ma.tricol.supplies.repository;

import ma.tricol.supplies.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    Optional<Supplier> findByIce(String ice);
    
    Optional<Supplier> findByCompany(String company);
    
    @Query("SELECT s FROM Supplier s WHERE " +
           "(:city IS NULL OR s.city = :city) AND " +
           "(:search IS NULL OR LOWER(s.company) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.contact) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Supplier> findByFilters(@Param("city") String city, 
                                  @Param("search") String search, 
                                  Pageable pageable);
}
