package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.dto.AddressResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class AddressDTOMapper implements Function<Address, AddressResponseDTO> {
    @Override
    public AddressResponseDTO apply(Address address) {

        return new AddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getCountry(),
                address.getPostalCode()
        );
    }
}
