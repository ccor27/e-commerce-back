package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.HistoryException;
import com.ccor.ecommerce.model.History;
import com.ccor.ecommerce.model.dto.HistoryRequestDTO;
import com.ccor.ecommerce.model.dto.HistoryResponseDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import com.ccor.ecommerce.service.IHistoryService;
import com.ccor.ecommerce.service.export.excel.IExportExcelService;
import com.ccor.ecommerce.service.export.pdf.IExportPdfService;
import com.lowagie.text.DocumentException;
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
@RequestMapping("/api/v1/history")
public class HistoryController {
    @Autowired
    private IHistoryService iHistoryService;
    @Qualifier("History")
    @Autowired
    private IExportExcelService iExportExcelService;
    @Qualifier("History")
    @Autowired
    private IExportPdfService iExportPdfService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody HistoryRequestDTO historyRequestDTO){
     try {
         HistoryResponseDTO historyResponseDTO = iHistoryService.save(historyRequestDTO);
         return new ResponseEntity<>(historyResponseDTO, HttpStatus.CREATED);
     }catch (HistoryException ex){
         return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
     }

    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable("id")Long id){
     try {
         iHistoryService.remove(id);
         return new ResponseEntity<>(HttpStatus.OK);
     }catch (HistoryException ex){
         return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
     }

    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id")Long id){
        try {
            HistoryResponseDTO historyResponseDTO = iHistoryService.findById(id);
            return new ResponseEntity<>(historyResponseDTO,HttpStatus.FOUND);
        }catch (HistoryException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/find/{offset}/{pageSize}")
    public ResponseEntity<?> findAll(@PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<HistoryResponseDTO> list = iHistoryService.findAll(offset, pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (HistoryException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try {
            List<HistoryResponseDTO> list = iHistoryService.findAll(0, 10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (HistoryException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/find/sales/{offset}/{pageSize}")
    public ResponseEntity<?> findSales(@PathVariable("id")Long id, @PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<SaleResponseDTO> list = iHistoryService.findSales(id,offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (HistoryException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/find/sales")
    public ResponseEntity<?> findSalesByDefault(@PathVariable("id")Long id){
        try {
            List<SaleResponseDTO> list = iHistoryService.findSales(id,0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (HistoryException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id_history}/remove/sale/{id_sale}")
    public ResponseEntity<?> removeSale(@PathVariable("id_sale")Long id_sale,@PathVariable("id_history")Long id_history){
        try {
            iHistoryService.removeSale(id_sale,id_history);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (HistoryException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
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
        List<History> histories = iHistoryService.findAllToExport();
        iExportExcelService.generateExcelFile(response,histories);
    }
    @GetMapping("/export/all/{offset}/{pageSize}")
    public void exportIntoExcelFile(HttpServletResponse response, @PathVariable Integer offset, @PathVariable Integer pageSize) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<History> histories = iHistoryService.findAllToExport(offset,pageSize);
        iExportExcelService.generateExcelFile(response,histories);
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
