package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.dto.CreditCardRequestDTO;
import com.ccor.ecommerce.model.dto.CreditCardResponseDTO;
import com.ccor.ecommerce.service.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/card")
public class CreditCardController {
    @Autowired
    private ICreditCardService iCreditCardService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CreditCardRequestDTO requestDTO){
        CreditCardResponseDTO responseDTO = iCreditCardService.save(requestDTO);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody CreditCardRequestDTO requestDTO, @Param("id") Long id){
        CreditCardResponseDTO responseDTO = iCreditCardService.edit(requestDTO,id);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
     @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@Param("id") Long id){
        CreditCardResponseDTO responseDTO = iCreditCardService.findById(id);
         if(responseDTO!=null){
             return new ResponseEntity<>(responseDTO, HttpStatus.FOUND);
         }else{
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
     }
    @GetMapping("/find/number/{number}")
    public ResponseEntity<?> findByNumber(@Param("number") String number){
        CreditCardResponseDTO responseDTO = iCreditCardService.findCardByNumber(number);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO, HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAll(){
        List<CreditCardResponseDTO> list = iCreditCardService.findAll();
        if (list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/type/{type}")
    public ResponseEntity<?> findByType(@Param("typeCard") String type){
        List<CreditCardResponseDTO> list = iCreditCardService.findCardsByType(type);
        if (list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@Param("id") Long id){
        if(iCreditCardService.remove(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}