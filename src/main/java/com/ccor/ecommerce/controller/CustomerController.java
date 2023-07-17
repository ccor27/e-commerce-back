package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private ICustomerService iCustomerService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CustomerRequestDTO requestDTO){
        CustomerResponseDTO customerResponseDTO = iCustomerService.save(requestDTO);
        if(customerResponseDTO!=null){
            return new ResponseEntity<>(customerResponseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@Param("id") Long id){
        if(iCustomerService.remove(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody CustomerRequestEditDTO customerRequestEditDTO, @Param("id") Long id){
        CustomerResponseDTO responseDTO = iCustomerService.editData(customerRequestEditDTO,id);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("find/{id}")
    public ResponseEntity<?> findById(@Param("id") Long id){
        if(iCustomerService.remove(id)){
            return new ResponseEntity<>(HttpStatus.OK);
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
    @GetMapping("/find/history")
    public ResponseEntity<?> findHistory(@Param("id")Long id){
        HistoryResponseDTO historyResponseDTO = iCustomerService.findHistory(id);
        if(historyResponseDTO!=null){
            return new ResponseEntity<>(historyResponseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/address")
    public ResponseEntity<?> findAddress(@Param("id")Long id){
        List<AddressResponseDTO> addressResponseDTO = iCustomerService.findAddress(id);
        if(addressResponseDTO!=null){
            return new ResponseEntity<>(addressResponseDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/cards")
    public ResponseEntity<?> fincCards(@Param("id")Long id){
        List<CreditCardResponseDTO> list = iCustomerService.findCards(id);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/email/{email}")
    public ResponseEntity<?> findByEmail(@Param("email")String email){
        CustomerResponseDTO responseDTO = iCustomerService.findByEmail(email);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/change/pwd/{pwd}")
    public ResponseEntity<?> changePwd(@Param("id")Long id, @Param("pwd") String pwd){
        CustomerResponseDTO customerResponseDTO = iCustomerService.changePwd(pwd,id);
        if(customerResponseDTO!=null){
            return new ResponseEntity<>(customerResponseDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/add/address")
    public ResponseEntity<?> addAddress(@RequestBody AddressResponseDTO addressResponseDTO, @Param("id")Long id){
        List<AddressResponseDTO> list = iCustomerService.addAddress(addressResponseDTO,id);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //TODO:test this method and search information about @Param
    @DeleteMapping("/{id_customer}/remove/address/{id_address}")
    public ResponseEntity<?> removeAddress(@Param("id") Long id_customer, @Param("id") Long id_address){
        if(iCustomerService.removeAddress(id_address,id_customer)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/add/card")
    public ResponseEntity<?> addCreditCard(@RequestBody CreditCardResponseDTO creditCardResponseDTO, @Param("id")Long id){
        List<CreditCardResponseDTO> list = iCustomerService.addCreditCard(creditCardResponseDTO,id);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //TODO:test this method and search information about @Param
    @DeleteMapping("/{id_customer}/remove/card/{id_card}")
     public ResponseEntity<?> removeCard(@Param("id") Long id_customer,@Param("id")Long id_card){
        if(iCustomerService.removeCreditCard(id_card,id_customer)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
