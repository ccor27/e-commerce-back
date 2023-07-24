package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.dto.ProductStockRequestDTO;
import com.ccor.ecommerce.model.dto.ProductStockResponseDTO;
import com.ccor.ecommerce.service.IProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productStock")
public class ProductStockController {
    @Autowired
    private IProductStockService iProductStockService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProductStockRequestDTO productStockRequestDTO){
        ProductStockResponseDTO productStockResponseDTO = iProductStockService.save(productStockRequestDTO);
        if(productStockResponseDTO!=null){
            return new ResponseEntity<>(productStockResponseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@Param("id")Long id){
        if (iProductStockService.remove(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody ProductStockRequestDTO productStockRequestDTO, @Param("id") Long id){
        ProductStockResponseDTO productStockResponseDTO = iProductStockService.edit(productStockRequestDTO,id);
        if(productStockResponseDTO!=null){
            return new ResponseEntity<>(productStockResponseDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAll(){
        List<ProductStockResponseDTO> list = iProductStockService.findAll();
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@Param("id") Long id){
        ProductStockResponseDTO productStockResponseDTO = iProductStockService.findProductById(id);
        if(productStockResponseDTO!=null){
            return new ResponseEntity<>(productStockResponseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/enable")
    public ResponseEntity<?> findProductsStockByEnableProduct(){
        List<ProductStockResponseDTO> list = iProductStockService.findProductStocksByEnableProduct();
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{barCode}")
    public ResponseEntity<?> findProductStocksByBarCode(@Param("barCode")String barCode){
        ProductStockResponseDTO productStockResponseDTO = iProductStockService.findProductStocksByBarCode(barCode);
        if(productStockResponseDTO!=null){
            return new ResponseEntity<>(productStockResponseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/sell/{amount}")
    public ResponseEntity<?> sellProduct(@Param("id")Long id,@Param("amount") int amount){
        ProductStockResponseDTO productStockResponseDTO = iProductStockService.sellProduct(amount,id);
        if(productStockResponseDTO!=null){
            return new ResponseEntity<>(productStockResponseDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
