package com.ccor.ecommerce.service.registration;

import com.ccor.ecommerce.model.dto.AuthenticationResponseDTO;
import com.ccor.ecommerce.model.dto.CustomerRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IRegistrationService {
    public AuthenticationResponseDTO save(CustomerRequestDTO requestDTO, MultipartFile picture) throws IllegalAccessException;
    public String confirmToken(String token);
}
