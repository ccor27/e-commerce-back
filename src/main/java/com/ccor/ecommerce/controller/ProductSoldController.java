package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.ProductSoldException;
import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.service.IProductSoldService;
import com.ccor.ecommerce.service.export.excel.IExportExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productSold")
public class ProductSoldController {
    @Autowired
    private IProductSoldService iProductSoldService;
    @Qualifier("ProductSold")
    @Autowired
    private IExportExcelService iExportExcelService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProductSoldRequestDTO productSoldRequestDTO){
        try {
            ProductSoldResponseDTO productSoldResponseDTO = iProductSoldService.save(productSoldRequestDTO);
            return new ResponseEntity<>(productSoldResponseDTO, HttpStatus.CREATED);
        }catch (ProductSoldException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id")Long id){
        try {
            ProductSoldResponseDTO productSoldResponseDTO = iProductSoldService.findById(id);
            return new ResponseEntity<>(productSoldResponseDTO,HttpStatus.FOUND);
        }catch (ProductSoldException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{offset}/{pageSize}")
    public ResponseEntity<?> findAll(@PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<ProductSoldResponseDTO> list = iProductSoldService.findAll(offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (ProductSoldException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try {
            List<ProductSoldResponseDTO> list = iProductSoldService.findAll(0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (ProductSoldException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/barCode/{barcode}/{offset}/{pageSize}")
     public ResponseEntity<?> findProductsSoldByBarCode(@PathVariable("barcode")String barCode,@PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<ProductSoldResponseDTO> list = iProductSoldService.findProductsSoldByBarCode(barCode,offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (ProductSoldException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/barCode/{barcode}")
    public ResponseEntity<?> findProductsSoldByBarCodeByDefault(@PathVariable("barcode")String barCode){
        try {
            List<ProductSoldResponseDTO> list = iProductSoldService.findProductsSoldByBarCode(barCode,0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (ProductSoldException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/export/all/{offset}/{pageSize}")
    public void exportIntoExcelFileAll(HttpServletResponse response,@PathVariable Integer offset, @PathVariable Integer pageSize) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<ProductSold> customers = iProductSoldService.findAllToExport(offset, pageSize);
        iExportExcelService.generateExcelFile(response,customers);
    }
    @GetMapping("/export/all")
    public void exportIntoExcelFileAll(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<ProductSold> customers = iProductSoldService.findAllToExport();
        iExportExcelService.generateExcelFile(response,customers);
    }
}
