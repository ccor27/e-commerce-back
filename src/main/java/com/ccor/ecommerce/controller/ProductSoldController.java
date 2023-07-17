package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.service.IProductSoldService;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productSold")
public class ProductSoldController {
    @Autowired
    private IProductSoldService iProductSoldService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProductSoldRequestDTO productSoldRequestDTO){
        ProductSoldResponseDTO productSoldResponseDTO = iProductSoldService.save(productSoldRequestDTO);
        if(productSoldResponseDTO!=null){
           return new ResponseEntity<>(productSoldResponseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody ProductSoldRequestDTO productSoldRequestDTO, @Param("id")Long id){
        ProductSoldResponseDTO productSoldResponseDTO = iProductSoldService.edit(productSoldRequestDTO,id);
        if(productSoldResponseDTO!=null){
            return new ResponseEntity<>(productSoldResponseDTO, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@Param("id")Long id){
        if(iProductSoldService.remove(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@Param("id")Long id){
        ProductSoldResponseDTO productSoldResponseDTO = iProductSoldService.findById(id);
        if(productSoldResponseDTO!=null){
            return new ResponseEntity<>(productSoldResponseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAll(){
        List<ProductSoldResponseDTO> list = iProductSoldService.findAll();
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/barCode/{barcode}")
     public ResponseEntity<?> findProductsSoldByBarCode(@Param("barCode")String barCode){
        List<ProductSoldResponseDTO> list = iProductSoldService.findProductsSoldByBarCode(barCode);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
