package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.SaleException;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.service.ISaleService;
import com.ccor.ecommerce.service.export.excel.IExportExcelService;
import com.ccor.ecommerce.service.export.pdf.IExportPdfService;
import com.lowagie.text.DocumentException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {

    private ISaleService iSaleService;
    private IExportExcelService iExportExcelService;
    private IExportPdfService iExportPdfService;
    @Autowired
    public SaleController(
            ISaleService iSaleService,
            @Qualifier("Sale") IExportExcelService iExportExcelService,
            @Qualifier("Sale")IExportPdfService iExportPdfService) {
        this.iSaleService = iSaleService;
        this.iExportExcelService = iExportExcelService;
        this.iExportPdfService = iExportPdfService;
    }

    @PostMapping("/{customerId}/save")
    public ResponseEntity<?> save(@RequestBody SaleRequestDTO saleRequestDTO,
                                  @PathVariable("customerId") Long customerId){
        try {
            SaleResponseDTO saleResponseDTO = iSaleService.save(saleRequestDTO,customerId);
            return new ResponseEntity<>(saleResponseDTO, HttpStatus.CREATED);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
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
    @GetMapping("/find/date/{date}/{offset}/{pageSize}")
    public ResponseEntity<?> findSalesByDate(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @PathVariable Integer offset, @PathVariable Integer pageSize ){
        try{
            List<SaleResponseDTO> list = iSaleService.findSalesByDate(date,offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/date/{date}")
    public ResponseEntity<?> findSalesByDateByDefault(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date ){
        try{
            List<SaleResponseDTO> list = iSaleService.findSalesByDate(date,0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/date/{start}/{end}/{offset}/{pageSize}")
    public ResponseEntity<?> findSalesByDateRange(
            @PathVariable("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @PathVariable("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
            @PathVariable Integer offset, @PathVariable Integer pageSize ){
        try{
            List<SaleResponseDTO> list = iSaleService.findSalesBetweenDate(start,end,offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/date/{start}/{end}")
    public ResponseEntity<?> findSalesByDateRangeByDefault(
            @PathVariable("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @PathVariable("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end){
        try{
            List<SaleResponseDTO> list = iSaleService.findSalesBetweenDate(start,end,0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
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
    @GetMapping("/find/{id}/payment")
    public ResponseEntity<?> findPayment(@PathVariable("id")Long id){
        try {
            PaymentResponseDTO responseDTO = iSaleService.findPayment(id);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (SaleException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
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
        List<Sale> sales = iSaleService.findAllToExport(offset,pageSize);
        iExportExcelService.generateExcelFile(response,sales);
    }
    @GetMapping("/export/all")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Sale> sales = iSaleService.findAllToExport();
        iExportExcelService.generateExcelFile(response,sales);
    }
    @GetMapping("/{id}/export/pdf")
    public void exportToPDF(@PathVariable Long id, HttpServletResponse response) throws DocumentException,Exception {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        iExportPdfService.export(response,id);
    }
}
