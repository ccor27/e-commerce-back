package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.CustomerException;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.service.ICustomerService;
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
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private ICustomerService iCustomerService;
    @Qualifier("Customer")
    @Autowired
    private IExportExcelService iExportExcelService;
    @Qualifier("Customer")
    @Autowired
    private IExportPdfService iExportPdfService;

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable("id") Long id){
        try {
            iCustomerService.removeCustomer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @PatchMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody CustomerRequestEditDTO customerRequestEditDTO, @PathVariable("id") Long id){
        try{
            CustomerResponseDTO responseDTO = iCustomerService.editData(customerRequestEditDTO,id);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        try {
            CustomerResponseDTO responseDTO = iCustomerService.findById(id);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/find/{offset}/{pageSize}")
    public ResponseEntity<?> findAll(@PathVariable Integer offset,@PathVariable Integer pageSize){
        try{
            List<CustomerResponseDTO> list = iCustomerService.findAll(offset, pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try{
            List<CustomerResponseDTO> list = iCustomerService.findAll(0, 10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/history")
    public ResponseEntity<?> findHistory(@PathVariable("id")Long id){
        try{
            HistoryResponseDTO historyResponseDTO = iCustomerService.findHistory(id);
            return new ResponseEntity<>(historyResponseDTO,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/find/sales")
    public ResponseEntity<?> findCustomerSales(@PathVariable("id") Long id){
        try{
            List<SaleResponseDTO> list = iCustomerService.findCustomerSales(id);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/find/payments")
    public ResponseEntity<?> findCustomerPayments(@PathVariable("id") Long id){
        try{
            List<PaymentResponseDTO> list = iCustomerService.findCustomerPayments(id);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/address/{offset}/{pageSize}")
    public ResponseEntity<?> findAddress(@PathVariable("id")Long id,@PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<AddressResponseDTO> addressResponseDTO = iCustomerService.findAddress(id,offset,pageSize);
            return new ResponseEntity<>(addressResponseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/address")
    public ResponseEntity<?> findAddressByDefault(@PathVariable("id")Long id){
        try {
            List<AddressResponseDTO> addressResponseDTO = iCustomerService.findAddress(id,0,10);
            return new ResponseEntity<>(addressResponseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/cards/{offset}/{pageSize}")
    public ResponseEntity<?> findCards(@PathVariable("id")Long id, @PathVariable Integer offset,@PathVariable Integer pageSize){
        try {
            List<CreditCardResponseDTO> list = iCustomerService.findCards(id,offset,pageSize);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{id}/cards")
    public ResponseEntity<?> findCardsByDefault(@PathVariable("id")Long id){
        try {
            List<CreditCardResponseDTO> list = iCustomerService.findCards(id,0,10);
            return new ResponseEntity<>(list,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable("email")String email){
        try{
            CustomerResponseDTO responseDTO = iCustomerService.findByEmail(email);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @PutMapping("/{id}/change/pwd")
    public ResponseEntity<?> changePwd(@RequestBody ChangePwdRequestDTO changePwdRequestDTO){
        try{
            CustomerResponseDTO customerResponseDTO = iCustomerService.changePwd(changePwdRequestDTO);
            return new ResponseEntity<>(customerResponseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @PatchMapping("/{id}/change/username")
    public ResponseEntity<?> changeUsername(@RequestBody ChangeUsernameRequestDTO changeUsernameRequestDTO){
        try{
            CustomerResponseDTO customerResponseDTO = iCustomerService.changeUsername(changeUsernameRequestDTO);
            return new ResponseEntity<>(customerResponseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/add/address")
    public ResponseEntity<?> addAddress(@RequestBody AddressResponseDTO addressResponseDTO, @PathVariable("id")Long id){
        try{
            List<AddressResponseDTO> list = iCustomerService.addAddress(addressResponseDTO,id);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PatchMapping("/{idCustomer}/edit/address/{idAddress}")
    public ResponseEntity<?> editAddress(@RequestBody AddressEditRequestDTO requestDTO, @PathVariable("id")Long idCustomer,@PathVariable("id")Long idAddress){
        try{
            AddressResponseDTO responseDTO = iCustomerService.updateDataAddress(idCustomer,idAddress,requestDTO);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id_customer}/remove/address/{id_address}")
    public ResponseEntity<?> removeAddress(@PathVariable("id_customer") Long id_customer, @PathVariable("id_address") Long id_address){
        try {
            iCustomerService.removeAddress(id_address,id_customer);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/add/card")
    public ResponseEntity<?> addCreditCard(@RequestBody CreditCardResponseDTO creditCardResponseDTO, @PathVariable("id")Long id){
       try {
           List<CreditCardResponseDTO> list = iCustomerService.addCreditCard(creditCardResponseDTO,id);
           return new ResponseEntity<>(list,HttpStatus.OK);
       }catch (CustomerException ex){
           return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }
    @PatchMapping("/{idCustomer}/edit/address/{idCard}")
    public ResponseEntity<?> editCreditCard(@RequestBody CreditCardEditRequestDTO requestDTO, @PathVariable("id")Long idCustomer,@PathVariable("id")Long idCard){
        try{
            CreditCardResponseDTO responseDTO = iCustomerService.updateDataCreditCard(idCustomer,idCard,requestDTO);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
        @DeleteMapping("/{id_customer}/remove/card/{id_card}")
     public ResponseEntity<?> removeCard(@PathVariable("id_customer") Long id_customer,@PathVariable("id_card")Long id_card){
        try {
            iCustomerService.removeCreditCard(id_card,id_customer);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/find/by/tk/{token}")
    public ResponseEntity<?> findCustomerByToken(@PathVariable("token")String token){
        try {
            CustomerResponseDTO responseDTO = iCustomerService.getCustomerByToken(token);
            return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
        }catch (CustomerException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/export/all")
    public void exportIntoExcelFileAll(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Customer> customers = iCustomerService.findAllToExport();
        iExportExcelService.generateExcelFile(response,customers);
    }
    @GetMapping("/export/all/{offset}/{pageSize}")
    public void exportIntoExcelFile(HttpServletResponse response,@PathVariable Integer offset,@PathVariable Integer pageSize) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Customer> customers = iCustomerService.findAllToExport(offset,pageSize);
        iExportExcelService.generateExcelFile(response,customers);
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
