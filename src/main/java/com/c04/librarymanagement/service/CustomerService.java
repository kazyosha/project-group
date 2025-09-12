package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.CustomerDTO;
import com.c04.librarymanagement.model.Customer;
import com.c04.librarymanagement.repository.CustomerRepository;
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

    public CustomerDTO addCustomer(CustomerDTO dto) {
        Customer customer = toEntity(dto);
        Customer saved = customerRepository.save(customer);
        return toDTO(saved);
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .filter(c -> !c.getDeleted())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CustomerDTO> getCustomer(Long id) {
        return customerRepository.findById(id)
                .filter(c -> !c.getDeleted())
                .map(this::toDTO);
    }

}
