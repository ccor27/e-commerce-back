package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.SaleException;
import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.model.dto.SaleRequestDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import com.ccor.ecommerce.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {
    @Autowired
    private ISaleService iSaleService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SaleRequestDTO saleRequestDTO){
        try {
            SaleResponseDTO saleResponseDTO = iSaleService.save(saleRequestDTO);
            return new ResponseEntity<>(saleResponseDTO, HttpStatus.CREATED);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody SaleRequestDTO saleRequestDTO, @PathVariable("id")Long id){
        try {
            SaleResponseDTO responseDTO = iSaleService.edit(saleRequestDTO,id);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id")Long id){
      try {
          SaleResponseDTO responseDTO = iSaleService.findById(id);
          return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
      }catch (SaleException ex){
          return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
      }
    }
    @GetMapping("/find/{offset}/{pageSize}")
    public ResponseEntity<?> findAll(@PathVariable Integer offset,@PathVariable Integer pageSize){
       try {
           List<SaleResponseDTO> list = iSaleService.findAll(offset, pageSize);
           return new ResponseEntity<>(list,HttpStatus.FOUND);
       }catch (SaleException ex){
           return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
       }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try {
            List<SaleResponseDTO> list = iSaleService.findAll(0, 10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/add/product")
    public ResponseEntity<?> addProduct(@RequestBody ProductSoldRequestDTO productSoldRequestDTO,@PathVariable("id")Long  id){
       try {
           SaleResponseDTO responseDTO=iSaleService.addProductSold(productSoldRequestDTO,id);
           return new ResponseEntity<>(responseDTO,HttpStatus.OK);
       }catch (SaleException ex){
           return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }
    @DeleteMapping("/{id_sale}/remove/product/{id_product}")
    public ResponseEntity<?> removeProduct(@PathVariable("id_sale")Long id_sale,@PathVariable("id_product")Long id_product){
        try {
            SaleResponseDTO responseDTO = iSaleService.removeProductSold(id_product,id_sale);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/{id}/find/products/{offset}/{pageSize}")
    public ResponseEntity<?> findProducts(@PathVariable("id")Long id,@PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<ProductSoldResponseDTO> list = iSaleService.findProductsSold(id,offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/find/products")
    public ResponseEntity<?> findProductsByDefault(@PathVariable("id")Long id){
        try {
            List<ProductSoldResponseDTO> list = iSaleService.findProductsSold(id,0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}
