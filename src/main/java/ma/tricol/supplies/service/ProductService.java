package ma.tricol.supplies.service;

import ma.tricol.supplies.dto.ProductDTO;
import ma.tricol.supplies.exception.ResourceNotFoundException;
import ma.tricol.supplies.mapper.ProductMapper;
import ma.tricol.supplies.model.Product;
import ma.tricol.supplies.model.StockMovement;
import ma.tricol.supplies.model.enums.MovementType;
import ma.tricol.supplies.repository.ProductRepository;
import ma.tricol.supplies.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO);
    }

    public Page<ProductDTO> getProductsByFilters(String category, String search, Pageable pageable) {
        return productRepository.findByFilters(category, search, pageable)
                .map(productMapper::toDTO);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Integer initialStock = product.getCurrentStock() != null ? product.getCurrentStock() : 0;
        product.setCurrentStock(initialStock);
        
        Product savedProduct = productRepository.save(product);
        
        if (initialStock > 0) {
            StockMovement movement = new StockMovement();
            movement.setMovementDate(LocalDateTime.now());
            movement.setQuantity(initialStock);
            movement.setMovementType(MovementType.ENTREE);
            movement.setUnitPrice(savedProduct.getUnitPrice());
            movement.setProduct(savedProduct);
            movement.setOrder(null);
            stockMovementRepository.save(movement);
        }
        
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        
        Integer oldStock = existingProduct.getCurrentStock();
        
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setUnitPrice(productDTO.getUnitPrice());
        existingProduct.setCategory(productDTO.getCategory());
        
        // If currentStock is provided in DTO and different, create AJUSTEMENT movement
        if (productDTO.getCurrentStock() != null && !productDTO.getCurrentStock().equals(oldStock)) {
            Integer newStock = productDTO.getCurrentStock();
            Integer difference = newStock - oldStock;
            
            existingProduct.setCurrentStock(newStock);
            
            // Create AJUSTEMENT movement
            StockMovement movement = new StockMovement();
            movement.setMovementDate(LocalDateTime.now());
            movement.setQuantity(Math.abs(difference));
            movement.setMovementType(MovementType.AJUSTEMENT);
            movement.setUnitPrice(existingProduct.getUnitPrice());
            movement.setProduct(existingProduct);
            movement.setOrder(null);
            stockMovementRepository.save(movement);
        }
        
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
    }
}
