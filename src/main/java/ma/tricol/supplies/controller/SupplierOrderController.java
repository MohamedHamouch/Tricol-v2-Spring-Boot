package ma.tricol.supplies.controller;

import ma.tricol.supplies.dto.SupplierOrderDTO;
import ma.tricol.supplies.model.enums.OrderStatus;
import ma.tricol.supplies.service.SupplierOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class SupplierOrderController {

    @Autowired
    private SupplierOrderService supplierOrderService;

    @GetMapping
    public ResponseEntity<Page<SupplierOrderDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) OrderStatus status) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SupplierOrderDTO> orders = supplierOrderService.getOrdersByFilters(supplierId, status, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierOrderDTO> getOrderById(@PathVariable Long id) {
        SupplierOrderDTO order = supplierOrderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierOrderDTO>> getOrdersBySupplierId(@PathVariable Long supplierId) {
        List<SupplierOrderDTO> orders = supplierOrderService.getOrdersBySupplierId(supplierId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<SupplierOrderDTO> createOrder(@Validated @RequestBody SupplierOrderDTO orderDTO) {
        SupplierOrderDTO createdOrder = supplierOrderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierOrderDTO> updateOrder(
            @PathVariable Long id,
            @Validated @RequestBody SupplierOrderDTO orderDTO) {
        SupplierOrderDTO updatedOrder = supplierOrderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SupplierOrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        SupplierOrderDTO updatedOrder = supplierOrderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        supplierOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
