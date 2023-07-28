package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.dto.AuthenticationRequestDTO;
import com.ccor.ecommerce.model.dto.AuthenticationResponseDTO;
import com.ccor.ecommerce.model.dto.CustomerRequestDTO;
import com.ccor.ecommerce.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    @Autowired
    private ICustomerService iCustomerService;
    @PostMapping("/save")
    //@PreAuthorize("hasRole(ROLE.CUSTOMER) && hasRole(ROLE.CUSTOMER)")
    public ResponseEntity<?> save(@RequestBody CustomerRequestDTO requestDTO){
        AuthenticationResponseDTO authenticationResponseDTO = iCustomerService.save(requestDTO);
        if(authenticationResponseDTO!=null){
            return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO){
            AuthenticationResponseDTO responseDTO = iCustomerService.authenticate(authenticationRequestDTO);
            if(responseDTO!=null){
                return new ResponseEntity<>(responseDTO,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
   }
}
