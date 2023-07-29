package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private ICustomerService iCustomerService;

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable("id") Long id){
        if(iCustomerService.removeCustomer(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody CustomerRequestEditDTO customerRequestEditDTO, @PathVariable("id") Long id){
        CustomerResponseDTO responseDTO = iCustomerService.editData(customerRequestEditDTO,id);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        CustomerResponseDTO responseDTO = iCustomerService.findById(id);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAll(){
        List<CustomerResponseDTO> list = iCustomerService.findAll();
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/history")
    public ResponseEntity<?> findHistory(@PathVariable("id")Long id){
        HistoryResponseDTO historyResponseDTO = iCustomerService.findHistory(id);
        if(historyResponseDTO!=null){
            return new ResponseEntity<>(historyResponseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/address")
    public ResponseEntity<?> findAddress(@PathVariable("id")Long id){
        List<AddressResponseDTO> addressResponseDTO = iCustomerService.findAddress(id);
        if(addressResponseDTO!=null){
            return new ResponseEntity<>(addressResponseDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/cards")
    public ResponseEntity<?> findCards(@PathVariable("id")Long id){
        List<CreditCardResponseDTO> list = iCustomerService.findCards(id);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable("email")String email){
        CustomerResponseDTO responseDTO = iCustomerService.findByEmail(email);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/change/pwd/{pwd}")
    public ResponseEntity<?> changePwd(@PathVariable("id")Long id, @PathVariable("pwd") String pwd){
        CustomerResponseDTO customerResponseDTO = iCustomerService.changePwd(pwd,id);
        if(customerResponseDTO!=null){
            return new ResponseEntity<>(customerResponseDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/add/address")
    public ResponseEntity<?> addAddress(@RequestBody AddressResponseDTO addressResponseDTO, @PathVariable("id")Long id){
        List<AddressResponseDTO> list = iCustomerService.addAddress(addressResponseDTO,id);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //TODO:test this method and search information about @PathVariable
    @DeleteMapping("/{id_customer}/remove/address/{id_address}")
    public ResponseEntity<?> removeAddress(@PathVariable("id_customer") Long id_customer, @PathVariable("id_address") Long id_address){
        if(iCustomerService.removeAddress(id_address,id_customer)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/add/card")
    public ResponseEntity<?> addCreditCard(@RequestBody CreditCardResponseDTO creditCardResponseDTO, @PathVariable("id")Long id){
        List<CreditCardResponseDTO> list = iCustomerService.addCreditCard(creditCardResponseDTO,id);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
        @DeleteMapping("/{id_customer}/remove/card/{id_card}")
     public ResponseEntity<?> removeCard(@PathVariable("id_customer") Long id_customer,@PathVariable("id_card")Long id_card){
        if(iCustomerService.removeCreditCard(id_card,id_customer)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/find/by/tk/{token}")
    public ResponseEntity<?> findCustomerByToken(@PathVariable("token")String token){
        CustomerResponseDTO responseDTO = iCustomerService.getCustomerByToken(token);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
