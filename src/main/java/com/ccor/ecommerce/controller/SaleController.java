package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.model.dto.SaleRequestDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import com.ccor.ecommerce.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
        SaleResponseDTO saleResponseDTO = iSaleService.save(saleRequestDTO);
        if(saleResponseDTO!=null){
            return new ResponseEntity<>(saleResponseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody SaleRequestDTO saleRequestDTO, @Param("id")Long id){
        SaleResponseDTO responseDTO = iSaleService.edit(saleRequestDTO,id);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@Param("id")Long id){
        SaleResponseDTO responseDTO = iSaleService.findById(id);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAll(){
        List<SaleResponseDTO> list = iSaleService.findAll();
        if(list!=null){
         return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/add/product")
    public ResponseEntity<?> addProduct(@RequestBody ProductSoldRequestDTO productSoldRequestDTO,@Param("id")Long  id){
        SaleResponseDTO responseDTO=iSaleService.addProductSold(productSoldRequestDTO,id);
        if(responseDTO!=null){
         return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }else{
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id_sale}/remove/product/{id_product}")
    public ResponseEntity<?> removeProduct(@Param("id")Long id_sale,@Param("id")Long id_product){
        SaleResponseDTO responseDTO = iSaleService.removeProductSold(id_product,id_sale);
        if(responseDTO!=null){
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/find/product")
    public ResponseEntity<?> findProducts(@Param("id")Long id){
        List<ProductSoldResponseDTO> list = iSaleService.findProductsSold(id);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
