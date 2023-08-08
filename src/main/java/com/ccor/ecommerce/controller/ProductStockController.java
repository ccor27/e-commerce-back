package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.ProductStockException;
import com.ccor.ecommerce.model.dto.ProductStockRequestDTO;
import com.ccor.ecommerce.model.dto.ProductStockResponseDTO;
import com.ccor.ecommerce.service.IProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
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
        try{
            ProductStockResponseDTO productStockResponseDTO = iProductStockService.save(productStockRequestDTO);
                return new ResponseEntity<>(productStockResponseDTO, HttpStatus.CREATED);
        }catch (ProductStockException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable("id")Long id){
        try {
            iProductStockService.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ProductStockException ex) {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody ProductStockRequestDTO productStockRequestDTO, @PathVariable("id") Long id){
        try{
            ProductStockResponseDTO productStockResponseDTO = iProductStockService.edit(productStockRequestDTO,id);
            return new ResponseEntity<>(productStockResponseDTO,HttpStatus.OK);
        }catch (ProductStockException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try{
            List<ProductStockResponseDTO> list = iProductStockService.findAll(0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (ProductStockException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{offset}/{pageSize}")
        public ResponseEntity<?> findAll(@PathVariable Integer offset,@PathVariable Integer pageSize){
        try{
            List<ProductStockResponseDTO> list = iProductStockService.findAll(offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (ProductStockException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        try{
            ProductStockResponseDTO productStockResponseDTO = iProductStockService.findProductById(id);
            return new ResponseEntity<>(productStockResponseDTO,HttpStatus.FOUND);
        }catch (ProductStockException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }


    }
    @GetMapping("/find/enable")
    public ResponseEntity<?> findProductsStockByEnableProductByDefault(){
        try{
            List<ProductStockResponseDTO> list = iProductStockService.findProductStocksByEnableProduct(0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (ProductStockException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }


    }
    @GetMapping("/find/enable/{offset}/{pageSize}")
    public ResponseEntity<?> findProductsStockByEnableProduct(@PathVariable Integer offset,@PathVariable Integer pageSize){
        try{
            List<ProductStockResponseDTO> list = iProductStockService.findProductStocksByEnableProduct(offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (ProductStockException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/barcode/{barCode}")
    public ResponseEntity<?> findProductStockByBarCode(@PathVariable("barCode")String barCode){
        try {
            ProductStockResponseDTO productStockResponseDTO = iProductStockService.findProductStockByBarCode(barCode);
            return new ResponseEntity<>(productStockResponseDTO,HttpStatus.FOUND);
        }catch (ProductStockException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }


    }
    @PostMapping("/{id}/sell/{amount}")
    public ResponseEntity<?> sellProduct(@PathVariable("id")Long id,@PathVariable("amount") int amount) {
        try {
            ProductStockResponseDTO productStockResponseDTO = iProductStockService.sellProduct(amount, id);
            return new ResponseEntity<>(productStockResponseDTO, HttpStatus.OK);
        } catch (ProductStockException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
