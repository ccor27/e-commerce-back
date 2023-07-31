package com.ccor.ecommerce.service.registration;

import com.ccor.ecommerce.model.dto.AuthenticationResponseDTO;
import com.ccor.ecommerce.model.dto.CustomerRequestDTO;

public interface IRegistrationService {
    public AuthenticationResponseDTO save(CustomerRequestDTO requestDTO) throws IllegalAccessException;
    public String confirmToken(String token);
}
