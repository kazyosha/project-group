package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.CustomerDTO;
import com.c04.librarymanagement.model.Customer;
import com.c04.librarymanagement.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .code(customer.getCode())
                .name(customer.getName())
                .schoolClass(customer.getSchoolClass())
                .address(customer.getAddress())
                .birthDate(customer.getBirthDate())
                .build();
    }

    private Customer toEntity(CustomerDTO dto) {
        return Customer.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .schoolClass(dto.getSchoolClass())
                .address(dto.getAddress())
                .birthDate(dto.getBirthDate())
                .deleted(false)
                .build();
    }

    public CustomerDTO getAllCustomers (){
        return customerRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList()
                .get(0);
    }

    // Thêm khách hàng
    public CustomerDTO addCustomer(CustomerDTO dto) {
        Customer customer = toEntity(dto);
        Customer saved = customerRepository.save(customer);
        return toDTO(saved);
    }

    // Cập nhật khách hàng
    public CustomerDTO updateCustomer(CustomerDTO dto) {
        return customerRepository.findById(dto.getId())
                .filter(c -> !c.getDeleted())
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setCode(dto.getCode());
                    existing.setSchoolClass(dto.getSchoolClass());
                    existing.setAddress(dto.getAddress());
                    existing.setBirthDate(dto.getBirthDate());
                    Customer updated = customerRepository.save(existing);
                    return toDTO(updated);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng để cập nhật"));
    }

    // Lấy 1 khách hàng
    public Optional<CustomerDTO> findById(Long id) {
        return customerRepository.findById(id)
                .filter(c -> !c.getDeleted())
                .map(this::toDTO);
    }

    // Xóa mềm
    public boolean softDeleteCustomer(Long id) {
        return customerRepository.findById(id)
                .map(c -> {
                    c.setDeleted(true);
                    customerRepository.save(c);
                    return true;
                })
                .orElse(false);
    }

    // Khôi phục
    public boolean restoreCustomer(Long id) {
        return customerRepository.findById(id)
                .filter(Customer::getDeleted)
                .map(c -> {
                    c.setDeleted(false);
                    customerRepository.save(c);
                    return true;
                })
                .orElse(false);
    }

    // Xóa cứng
    public void hardDelete(Long id) {
        customerRepository.deleteById(id);
    }

    // Xóa toàn bộ thùng rác
    public void clearDeleted() {
        customerRepository.deleteAll(customerRepository.findByDeletedTrue());
    }

    // ==== Phân trang ====
    public Page<CustomerDTO> getCustomersPage(Pageable pageable) {
        return customerRepository.findByDeletedFalse(pageable)
                .map(this::toDTO);
    }

    public Page<CustomerDTO> getDeletedCustomersPage(Pageable pageable) {
        return customerRepository.findByDeletedTrue(pageable)
                .map(this::toDTO);
    }

    public List<CustomerDTO> searchByNameOrCode(String keyword) {
        return customerRepository.findAll().stream()
                .filter(c -> !c.getDeleted())
                .filter(c -> c.getName().toLowerCase().contains(keyword.toLowerCase())
                        || c.getCode().toLowerCase().contains(keyword.toLowerCase()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
