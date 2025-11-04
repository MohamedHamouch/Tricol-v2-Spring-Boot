package ma.tricol.supplies.service;

import ma.tricol.supplies.dto.SupplierOrderDTO;
import ma.tricol.supplies.exception.ResourceNotFoundException;
import ma.tricol.supplies.mapper.SupplierOrderMapper;
import ma.tricol.supplies.model.OrderProduct;
import ma.tricol.supplies.model.Product;
import ma.tricol.supplies.model.StockMovement;
import ma.tricol.supplies.model.SupplierOrder;
import ma.tricol.supplies.model.enums.MovementType;
import ma.tricol.supplies.model.enums.OrderStatus;
import ma.tricol.supplies.repository.ProductRepository;
import ma.tricol.supplies.repository.StockMovementRepository;
import ma.tricol.supplies.repository.SupplierOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierOrderService {

    @Autowired
    private SupplierOrderRepository supplierOrderRepository;

    @Autowired
    private SupplierOrderMapper supplierOrderMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    public List<SupplierOrderDTO> getAllOrders() {
        return supplierOrderRepository.findAll().stream()
                .map(supplierOrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<SupplierOrderDTO> getAllOrders(Pageable pageable) {
        return supplierOrderRepository.findAll(pageable)
                .map(supplierOrderMapper::toDTO);
    }

    public Page<SupplierOrderDTO> getOrdersByFilters(Long supplierId, OrderStatus status, Pageable pageable) {
        return supplierOrderRepository.findByFilters(supplierId, status, pageable)
                .map(supplierOrderMapper::toDTO);
    }

    public SupplierOrderDTO getOrderById(Long id) {
        SupplierOrder order = supplierOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierOrder", "id", id));
        return supplierOrderMapper.toDTO(order);
    }

    public List<SupplierOrderDTO> getOrdersBySupplierId(Long supplierId) {
        return supplierOrderRepository.findBySupplierId(supplierId).stream()
                .map(supplierOrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SupplierOrderDTO createOrder(SupplierOrderDTO orderDTO) {
        SupplierOrder order = supplierOrderMapper.toEntity(orderDTO);
        
        // Set default status if not provided
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.EN_ATTENTE);
        }
        
        // Calculate total amount from order products
        BigDecimal total = order.getOrderProducts().stream()
                .map(op -> op.getUnitPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
        
        SupplierOrder savedOrder = supplierOrderRepository.save(order);
        return supplierOrderMapper.toDTO(savedOrder);
    }

    public SupplierOrderDTO updateOrder(Long id, SupplierOrderDTO orderDTO) {
        SupplierOrder existingOrder = supplierOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierOrder", "id", id));
        
        OrderStatus oldStatus = existingOrder.getStatus();
        OrderStatus newStatus = orderDTO.getStatus();
        
        // Update status
        existingOrder.setStatus(newStatus);
        
        // Recalculate total amount
        BigDecimal total = existingOrder.getOrderProducts().stream()
                .map(op -> op.getUnitPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        existingOrder.setTotalAmount(total);
        
        // If status changed to LIVREE, create stock movements and update product stock
        if (oldStatus != OrderStatus.LIVREE && newStatus == OrderStatus.LIVREE) {
            createStockMovementsForOrder(existingOrder);
        }
        
        SupplierOrder updatedOrder = supplierOrderRepository.save(existingOrder);
        return supplierOrderMapper.toDTO(updatedOrder);
    }

    private void createStockMovementsForOrder(SupplierOrder order) {
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            StockMovement movement = new StockMovement();
            movement.setMovementDate(LocalDateTime.now());
            movement.setQuantity(orderProduct.getQuantity());
            movement.setMovementType(MovementType.ENTREE);
            movement.setUnitPrice(orderProduct.getUnitPrice());
            movement.setProduct(orderProduct.getProduct());
            movement.setOrder(order);
            
            stockMovementRepository.save(movement);
            
            Product product = orderProduct.getProduct();
            Integer currentStock = product.getCurrentStock() != null ? product.getCurrentStock() : 0;
            product.setCurrentStock(currentStock + orderProduct.getQuantity());
            productRepository.save(product);
        }
    }

    public SupplierOrderDTO updateOrderStatus(Long id, OrderStatus newStatus) {
        SupplierOrder existingOrder = supplierOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierOrder", "id", id));
        
        OrderStatus oldStatus = existingOrder.getStatus();
        existingOrder.setStatus(newStatus);
        
        // If status changed to LIVREE, create stock movements and update product stock
        if (oldStatus != OrderStatus.LIVREE && newStatus == OrderStatus.LIVREE) {
            createStockMovementsForOrder(existingOrder);
        }
        
        SupplierOrder updatedOrder = supplierOrderRepository.save(existingOrder);
        return supplierOrderMapper.toDTO(updatedOrder);
    }

    public void deleteOrder(Long id) {
        SupplierOrder order = supplierOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierOrder", "id", id));
        supplierOrderRepository.delete(order);
    }
}
