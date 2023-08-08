package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.CustomerException;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private ICustomerService iCustomerService;

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable("id") Long id){
        try {
            iCustomerService.removeCustomer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody CustomerRequestEditDTO customerRequestEditDTO, @PathVariable("id") Long id){
        try{
            CustomerResponseDTO responseDTO = iCustomerService.editData(customerRequestEditDTO,id);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        try {
            CustomerResponseDTO responseDTO = iCustomerService.findById(id);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/find/{offset}/{pageSize}")
    public ResponseEntity<?> findAll(@PathVariable Integer offset,@PathVariable Integer pageSize){
        try{
            List<CustomerResponseDTO> list = iCustomerService.findAll(offset, pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try{
            List<CustomerResponseDTO> list = iCustomerService.findAll(0, 10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/history")
    public ResponseEntity<?> findHistory(@PathVariable("id")Long id){
        try{
            HistoryResponseDTO historyResponseDTO = iCustomerService.findHistory(id);
            return new ResponseEntity<>(historyResponseDTO,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/address/{offset}/{pageSize}")
    public ResponseEntity<?> findAddress(@PathVariable("id")Long id,@PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<AddressResponseDTO> addressResponseDTO = iCustomerService.findAddress(id,offset,pageSize);
            return new ResponseEntity<>(addressResponseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/address")
    public ResponseEntity<?> findAddressByDefault(@PathVariable("id")Long id){
        try {
            List<AddressResponseDTO> addressResponseDTO = iCustomerService.findAddress(id,0,10);
            return new ResponseEntity<>(addressResponseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/cards/{offset}/{pageSize}")
    public ResponseEntity<?> findCards(@PathVariable("id")Long id, @PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<CreditCardResponseDTO> list = iCustomerService.findCards(id,offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/cards")
    public ResponseEntity<?> findCardsByDefault(@PathVariable("id")Long id){
        try {
            List<CreditCardResponseDTO> list = iCustomerService.findCards(id,0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable("email")String email){
        try{
            CustomerResponseDTO responseDTO = iCustomerService.findByEmail(email);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/{id}/change/pwd/{pwd}")
    public ResponseEntity<?> changePwd(@PathVariable("id")Long id, @PathVariable("pwd") String pwd){
        try{
            CustomerResponseDTO customerResponseDTO = iCustomerService.changePwd(pwd,id);
            return new ResponseEntity<>(customerResponseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/{id}/add/address")
    public ResponseEntity<?> addAddress(@RequestBody AddressResponseDTO addressResponseDTO, @PathVariable("id")Long id){
        try{
            List<AddressResponseDTO> list = iCustomerService.addAddress(addressResponseDTO,id);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    //TODO:test this method and search information about @PathVariable
    @DeleteMapping("/{id_customer}/remove/address/{id_address}")
    public ResponseEntity<?> removeAddress(@PathVariable("id_customer") Long id_customer, @PathVariable("id_address") Long id_address){
        try {
            iCustomerService.removeAddress(id_address,id_customer);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/add/card")
    public ResponseEntity<?> addCreditCard(@RequestBody CreditCardResponseDTO creditCardResponseDTO, @PathVariable("id")Long id){
       try {
           List<CreditCardResponseDTO> list = iCustomerService.addCreditCard(creditCardResponseDTO,id);
           return new ResponseEntity<>(list,HttpStatus.OK);
       }catch (CustomerException ex){
           return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }
        @DeleteMapping("/{id_customer}/remove/card/{id_card}")
     public ResponseEntity<?> removeCard(@PathVariable("id_customer") Long id_customer,@PathVariable("id_card")Long id_card){
        try {
            iCustomerService.removeCreditCard(id_card,id_customer);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/find/by/tk/{token}")
    public ResponseEntity<?> findCustomerByToken(@PathVariable("token")String token){
        try {
            CustomerResponseDTO responseDTO = iCustomerService.getCustomerByToken(token);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
}
