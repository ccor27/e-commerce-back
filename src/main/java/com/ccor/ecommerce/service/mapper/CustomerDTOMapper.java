package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.ChannelNotification;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.dto.CustomerResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                customer.getUsername(),
                channels(customer.getChannelNotifications()),
                customer.getPicturePath()
        );
    }
    private List<String> channels(List<ChannelNotification> channels){
        if(!channels.isEmpty()){
            return channels.stream().map(channel-> {
                switch (channel){
                    case EMAIL:
                        return "EMAIL";
                    case SMS:
                        return "SMS";
                    default: return null;
                }
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }
}
