package ma.tricol.supplies.service;

import ma.tricol.supplies.dto.SupplierDTO;
import ma.tricol.supplies.exception.ResourceNotFoundException;
import ma.tricol.supplies.mapper.SupplierMapper;
import ma.tricol.supplies.model.Supplier;
import ma.tricol.supplies.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<SupplierDTO> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable)
                .map(supplierMapper::toDTO);
    }

    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
        return supplierMapper.toDTO(supplier);
    }

    public SupplierDTO getSupplierByIce(String ice) {
        Supplier supplier = supplierRepository.findByIce(ice)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "ice", ice));
        return supplierMapper.toDTO(supplier);
    }

    public SupplierDTO getSupplierByCompany(String company) {
        Supplier supplier = supplierRepository.findByCompany(company)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "company", company));
        return supplierMapper.toDTO(supplier);
    }

    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDTO(savedSupplier);
    }

    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
        
        existingSupplier.setCompany(supplierDTO.getCompany());
        existingSupplier.setAddress(supplierDTO.getAddress());
        existingSupplier.setContact(supplierDTO.getContact());
        existingSupplier.setEmail(supplierDTO.getEmail());
        existingSupplier.setPhone(supplierDTO.getPhone());
        existingSupplier.setCity(supplierDTO.getCity());
        existingSupplier.setIce(supplierDTO.getIce());
        
        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return supplierMapper.toDTO(updatedSupplier);
    }

    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
        supplierRepository.delete(supplier);
    }
}
