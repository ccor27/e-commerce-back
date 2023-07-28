package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.dto.HistoryRequestDTO;
import com.ccor.ecommerce.model.dto.HistoryResponseDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import com.ccor.ecommerce.service.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {
    @Autowired
    private IHistoryService iHistoryService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody HistoryRequestDTO historyRequestDTO){
        HistoryResponseDTO historyResponseDTO = iHistoryService.save(historyRequestDTO);
        if(historyResponseDTO!=null){
            return new ResponseEntity<>(historyResponseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable("id")Long id){
        if(iHistoryService.remove(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id")Long id){
        HistoryResponseDTO historyResponseDTO = iHistoryService.findById(id);
        if(historyResponseDTO!=null){
            return new ResponseEntity<>(historyResponseDTO,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAll(){
        List<HistoryResponseDTO> list = iHistoryService.findAll();
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/find/sales")
    public ResponseEntity<?> findSales(@PathVariable("id")Long id){
        List<SaleResponseDTO> list = iHistoryService.findSales(id);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/add/sale")
    public ResponseEntity<?> addSale(@RequestBody SaleResponseDTO saleResponseDTO, @PathVariable("id")Long id){
        List<SaleResponseDTO> list = iHistoryService.addSale(saleResponseDTO,id);
        if(list!=null){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //TODO: search about  @Param
    @DeleteMapping("/{id_history}/remove/sale/{id_sale}")
    public ResponseEntity<?> removeSale(@PathVariable("id_sale")Long id_sale,@PathVariable("id_history")Long id_history){
        if(iHistoryService.removeSale(id_sale,id_history)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
