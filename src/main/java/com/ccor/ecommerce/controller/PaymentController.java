package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.PaymentException;
import com.ccor.ecommerce.model.Payment;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.service.IPaymentService;
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
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private IPaymentService iPaymentService;
    @Qualifier("Payment")
    @Autowired
    private IExportExcelService iExportExcelService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody PaymentRequestDTO paymentRequestDTO){
        try {
            PaymentResponseDTO paymentResponseDTO = iPaymentService.save(paymentRequestDTO);
            return new ResponseEntity<>(paymentResponseDTO,HttpStatus.OK);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/{customerId}/confirm/{paymentId}")
    public ResponseEntity<?> confirm(@PathVariable("paymentId")Long paymentId,
                                     @PathVariable("customerId") Long customerId){
        try{
            PaymentResponseDTO responseDTO = iPaymentService.confirm(paymentId,customerId);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{customerId}/cancel/{paymentId}")
    public ResponseEntity<?> cancel(@PathVariable("paymentId")Long paymentId,
                                    @PathVariable("customerId") Long customerId){
        try{
            PaymentResponseDTO responseDTO = iPaymentService.cancel(paymentId,customerId);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/find/id/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try {
            PaymentResponseDTO paymentResponseDTO = iPaymentService.findById(id);
            return new ResponseEntity<>(paymentResponseDTO,HttpStatus.FOUND);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try {
            List<PaymentResponseDTO> list = iPaymentService.findAll(0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{offset}/{pageSize}")
    public ResponseEntity<?> findAll(@PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<PaymentResponseDTO> list = iPaymentService.findAll(offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id){
        try {
            iPaymentService.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/status/{status}/{offset}/{pageSize}")
    public ResponseEntity<?> findPaymentsByStatus(@PathVariable Integer offset,@PathVariable Integer pageSize,@PathVariable String status){
        try {
            List<PaymentResponseDTO> list = iPaymentService.findPaymentsByStatusAndEnable(offset,pageSize,status);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/status/{status}")
    public ResponseEntity<?> findPaymentsByStatusDefault(@PathVariable String status){
        try {
            List<PaymentResponseDTO> list = iPaymentService.findPaymentsByStatusAndEnable(0,10,status);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/card")
    public ResponseEntity<?> findPaymentCard(@PathVariable Long id){
        try {
            CreditCardResponseDTO creditCardResponseDTO = iPaymentService.findCardPayment(id);
            return new ResponseEntity<>(creditCardResponseDTO,HttpStatus.FOUND);
        }catch (PaymentException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/export/all/{offset}/{pageSize}")
    public void exportIntoExcelFileAll(HttpServletResponse response, @PathVariable Integer offset, @PathVariable Integer pageSize) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Payment> customers = iPaymentService.findAllToExport(offset, pageSize);
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
        List<Payment> customers = iPaymentService.findAllToExport();
        iExportExcelService.generateExcelFile(response,customers);
    }
}
