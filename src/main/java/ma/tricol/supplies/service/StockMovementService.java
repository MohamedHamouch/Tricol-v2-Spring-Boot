package ma.tricol.supplies.service;

import ma.tricol.supplies.dto.StockMovementDTO;
import ma.tricol.supplies.exception.ResourceNotFoundException;
import ma.tricol.supplies.mapper.StockMovementMapper;
import ma.tricol.supplies.model.StockMovement;
import ma.tricol.supplies.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private StockMovementMapper stockMovementMapper;

    public List<StockMovementDTO> getAllMovements() {
        return stockMovementRepository.findAll().stream()
                .map(stockMovementMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<StockMovementDTO> getAllMovements(Pageable pageable) {
        return stockMovementRepository.findAll(pageable)
                .map(stockMovementMapper::toDTO);
    }

    public StockMovementDTO getMovementById(Long id) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockMovement", "id", id));
        return stockMovementMapper.toDTO(movement);
    }

    public Page<StockMovementDTO> getMovementsByProductId(Long productId, Pageable pageable) {
        return stockMovementRepository.findByProductId(productId, pageable)
                .map(stockMovementMapper::toDTO);
    }

    public StockMovementDTO createMovement(StockMovementDTO movementDTO) {
        StockMovement movement = stockMovementMapper.toEntity(movementDTO);
        StockMovement savedMovement = stockMovementRepository.save(movement);
        return stockMovementMapper.toDTO(savedMovement);
    }

    public void deleteMovement(Long id) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockMovement", "id", id));
        stockMovementRepository.delete(movement);
    }
}
