package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.CreditCardException;
import com.ccor.ecommerce.model.dto.CreditCardEditRequestDTO;
import com.ccor.ecommerce.model.dto.CreditCardRequestDTO;
import com.ccor.ecommerce.model.dto.CreditCardResponseDTO;
import com.ccor.ecommerce.service.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
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
        try {
            CreditCardResponseDTO responseDTO = iCreditCardService.save(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (CreditCardException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody CreditCardEditRequestDTO requestDTO, @PathVariable("id") Long id){
           try{
               CreditCardResponseDTO responseDTO = iCreditCardService.edit(requestDTO,id);
               return new ResponseEntity<>(responseDTO, HttpStatus.OK);
           }catch (CreditCardException ex){
               return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_MODIFIED);
           }


    }
     @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
       try{
           CreditCardResponseDTO responseDTO = iCreditCardService.findById(id);
           return new ResponseEntity<>(responseDTO, HttpStatus.FOUND);
       }catch (CreditCardException ex){
           return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
       }
     }
    @GetMapping("/find/number/{number}")
    public ResponseEntity<?> findByNumber(@PathVariable("number") String number){
        try {
            CreditCardResponseDTO responseDTO = iCreditCardService.findCardByNumber(number);
            return new ResponseEntity<>(responseDTO, HttpStatus.FOUND);
        }catch (CreditCardException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try {
            List<CreditCardResponseDTO> list = iCreditCardService.findAll(0, 10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CreditCardException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{offset}/{pageSize}")
    public ResponseEntity<?> findAll(@PathVariable Integer offset, @PathVariable Integer pageSize){
        try {
            List<CreditCardResponseDTO> list = iCreditCardService.findAll(offset, pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CreditCardException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/type/{type}/{offset}/{pageSize}")
    public ResponseEntity<?> findByType(@PathVariable("type") String type,@PathVariable Integer offset,@PathVariable Integer pageSize){
        try{
            List<CreditCardResponseDTO> list = iCreditCardService.findCardsByType(offset, pageSize, type);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CreditCardException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/type/{type}")
    public ResponseEntity<?> findByTypeByDefault(@PathVariable("type") String type){
        try{
            List<CreditCardResponseDTO> list = iCreditCardService.findCardsByType(0, 10, type);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CreditCardException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable("id") Long id){
        try{
            iCreditCardService.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (CreditCardException ex){
            return  new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

}