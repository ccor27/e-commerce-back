package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.ProductStockException;
import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.model.dto.ProductStockRequestDTO;
import com.ccor.ecommerce.model.dto.ProductStockResponseDTO;
import com.ccor.ecommerce.service.IProductStockService;
import com.ccor.ecommerce.service.export.excel.IExportExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productStock")
public class ProductStockController {
    @Autowired
    private IProductStockService iProductStockService;
    @Qualifier("ProductStock")
    @Autowired
    private IExportExcelService iExportExcelService;
//    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@RequestPart("productRequest") ProductStockRequestDTO productRequest,
                                  @RequestPart("picture") MultipartFile picture){
        try{

            ProductStockResponseDTO productStockResponseDTO = iProductStockService.save(productRequest,picture);
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
    @PostMapping(value = "/{id}/edit",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> edit(@RequestPart ProductStockRequestDTO productStockRequestDTO,
                                  @RequestPart("picture") MultipartFile picture,
                                  @PathVariable("id") Long id){
        try{
            ProductStockResponseDTO productStockResponseDTO = iProductStockService.edit(productStockRequestDTO,picture,id);
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
    @PostMapping("/{barCode}/sell/{amount}")
    public ResponseEntity<?> sellProduct(@PathVariable("barCode")String barCode,@PathVariable("amount") int amount) {
        try {
            ProductStockResponseDTO productStockResponseDTO = iProductStockService.sellProduct(amount, barCode);
            return new ResponseEntity<>(productStockResponseDTO, HttpStatus.OK);
        } catch (ProductStockException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/export/all/{offset}/{pageSize}")
    public void exportIntoExcelFile(HttpServletResponse response, @PathVariable Integer offset, @PathVariable Integer pageSize) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<ProductStock> customers = iProductStockService.findAllToExport(offset,pageSize);
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
        List<ProductStock> customers = iProductStockService.findAllToExport();
        iExportExcelService.generateExcelFile(response,customers);
    }
}
