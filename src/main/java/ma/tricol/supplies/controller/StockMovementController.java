package ma.tricol.supplies.controller;

import ma.tricol.supplies.dto.StockMovementDTO;
import ma.tricol.supplies.service.StockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    @Autowired
    private StockMovementService stockMovementService;

    @GetMapping
    public ResponseEntity<Page<StockMovementDTO>> getAllMovements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "movementDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<StockMovementDTO> movements = stockMovementService.getAllMovements(pageable);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementDTO> getMovementById(@PathVariable Long id) {
        StockMovementDTO movement = stockMovementService.getMovementById(id);
        return ResponseEntity.ok(movement);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<StockMovementDTO>> getMovementsByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("movementDate").descending());
        Page<StockMovementDTO> movements = stockMovementService.getMovementsByProductId(productId, pageable);
        return ResponseEntity.ok(movements);
    }

    @PostMapping
    public ResponseEntity<StockMovementDTO> createMovement(@Validated @RequestBody StockMovementDTO movementDTO) {
        StockMovementDTO createdMovement = stockMovementService.createMovement(movementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable Long id) {
        stockMovementService.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }
}
