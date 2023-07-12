package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.dto.AddressRequestDTO;
import com.ccor.ecommerce.model.dto.AddressResponseDTO;
import com.ccor.ecommerce.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/address")
@RestController
public class test {
    @Autowired
    private IAddressService iAddressService;
    @PostMapping("/save")
    public AddressResponseDTO save(@RequestBody AddressRequestDTO addressRequestDTO){
        return iAddressService.save(addressRequestDTO);
    }
}
