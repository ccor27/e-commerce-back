package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.AddressException;
import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.dto.AddressEditRequestDTO;
import com.ccor.ecommerce.model.dto.AddressRequestDTO;
import com.ccor.ecommerce.model.dto.AddressResponseDTO;
import com.ccor.ecommerce.service.IAddressService;
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
@RequestMapping("/api/v1/address")
public class AddressController {

    private IAddressService iAddressService;
    private IExportExcelService iExportExcelService;
    @Autowired
    public AddressController(IAddressService iAddressService,
                             @Qualifier("address") IExportExcelService iExportExcelService) {
        this.iAddressService = iAddressService;
        this.iExportExcelService = iExportExcelService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AddressRequestDTO addressRequestDTO){
        try {
            AddressResponseDTO addressResponseDTO = iAddressService.save(addressRequestDTO);
            return new ResponseEntity<>(addressResponseDTO, HttpStatus.CREATED);
        }catch (AddressException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody AddressEditRequestDTO addressRequestDTO, @PathVariable("id") Long id){
         try {
             AddressResponseDTO addressResponseDTO = iAddressService.edit(addressRequestDTO,id);
             return new ResponseEntity<>(addressResponseDTO, HttpStatus.CREATED);
         }catch (AddressException ex){
             return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
         }
    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@PathVariable("id") Long id){
        try {
            iAddressService.remove(id);
            return new ResponseEntity<>( HttpStatus.OK);
        }catch (AddressException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        try {
            AddressResponseDTO addressResponseDTO = iAddressService.findById(id);
            return new ResponseEntity<>(addressResponseDTO, HttpStatus.FOUND);
        } catch (AddressException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/find")
    public ResponseEntity<?> findAllByDefault(){
        try {
            List<AddressResponseDTO> list = iAddressService.findAll(0, 10);
            return new ResponseEntity<>(list, HttpStatus.FOUND);
        }catch (AddressException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/{offset}/{pageSize}")
    public ResponseEntity<?> findAll(@PathVariable Integer offset, @PathVariable Integer pageSize){
        try {
            List<AddressResponseDTO> list = iAddressService.findAll(offset, pageSize);
            return new ResponseEntity<>(list, HttpStatus.FOUND);
        }catch (AddressException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/pc/{postalCode}/{offset}/{pageSize}")
    public ResponseEntity<?> findByPostalCode(@PathVariable Integer offset, @PathVariable Integer pageSize,@PathVariable("postalCode") String postalCode){
        try{
            List<AddressResponseDTO> list = iAddressService.findAddressesByPostalCode(offset,pageSize,postalCode);
            return new ResponseEntity<>(list, HttpStatus.FOUND);
        }catch (AddressException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/find/pc/{postalCode}")
    public ResponseEntity<?> findByPostalCodeByDefault(@PathVariable("postalCode") String postalCode){
        try{
            List<AddressResponseDTO> list = iAddressService.findAddressesByPostalCode(0,10,postalCode);
            return new ResponseEntity<>(list, HttpStatus.FOUND);
        }catch (AddressException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/find/cnt/{country}/{offset}/{pageSize}")
    public ResponseEntity<?> findByCountry(@PathVariable Integer offset, @PathVariable Integer pageSize, @PathVariable("country") String country){
        try {
            List<AddressResponseDTO> list = iAddressService.findAddressesByCountry(offset,pageSize,country);
            return new ResponseEntity<>(list, HttpStatus.FOUND);
        }catch (AddressException ex){
            return new ResponseEntity<>(ex,HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/cnt/{country}")
    public ResponseEntity<?> findByCountryByDefault(@PathVariable("country") String country){
        try {
            List<AddressResponseDTO> list = iAddressService.findAddressesByCountry(0,10,country);
            return new ResponseEntity<>(list, HttpStatus.FOUND);
        }catch (AddressException ex){
            return new ResponseEntity<>(ex,HttpStatus.NOT_FOUND);
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
        List<Address> addresses = iAddressService.findAllToExport();
        iExportExcelService.generateExcelFile(response,addresses);
    }
    @GetMapping("/export/all/{offset}/{pageSize}")
    public void exportIntoExcelFile(HttpServletResponse response, @PathVariable Integer offset, @PathVariable Integer pageSize) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customer" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Address> addresses = iAddressService.findAllToExport(offset,pageSize);
        iExportExcelService.generateExcelFile(response,addresses);
    }
}
