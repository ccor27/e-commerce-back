package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.CancelSaleException;
import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.CanceledSale;
import com.ccor.ecommerce.model.dto.CanceledSaleResponseDTO;
import com.ccor.ecommerce.service.ICanceledSaleService;
import com.ccor.ecommerce.service.export.excel.IExportExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cancelled")
public class CanceledSaleController {

    @Autowired private ICanceledSaleService iCanceledSaleService;
    @Qualifier("canceledSale")
    @Autowired
    private IExportExcelService iExportExcelService;

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        try {
            CanceledSaleResponseDTO responseDTO = iCanceledSaleService.findById(id);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (CancelSaleException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try {
            List<CanceledSaleResponseDTO> responseDTO = iCanceledSaleService.findAll(0,10);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (CancelSaleException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{offset}/{pageSize}")
    public ResponseEntity<?> findAll(@PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize){
        try {
            List<CanceledSaleResponseDTO> responseDTO = iCanceledSaleService.findAll(offset,pageSize);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (CancelSaleException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/export/all")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<CanceledSale> canceledSales = iCanceledSaleService.findAllToExport();
        iExportExcelService.generateExcelFile(response,canceledSales);
    }
    @GetMapping("/export/all/{offset}/{pageSize}")
    public void exportIntoExcelFile(HttpServletResponse response, @PathVariable Integer offset, @PathVariable Integer pageSize) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<CanceledSale> canceledSales = iCanceledSaleService.findAllToExport(offset,pageSize);
        iExportExcelService.generateExcelFile(response,canceledSales);
    }
}
