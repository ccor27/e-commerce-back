package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.dto.CustomerResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class CustomerDTOMapper implements Function<Customer, CustomerResponseDTO> {
    @Override
    public CustomerResponseDTO apply(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getLastName(),
                customer.getCellphone(),
                customer.getEmail(),
                customer.getUsername()
        );
    }
}
