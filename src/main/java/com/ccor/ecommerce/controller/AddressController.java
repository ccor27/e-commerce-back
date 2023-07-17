package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.model.dto.AddressRequestDTO;
import com.ccor.ecommerce.model.dto.AddressResponseDTO;
import com.ccor.ecommerce.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/address")
@RestController
public class AddressController {
    @Autowired
    private IAddressService iAddressService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AddressRequestDTO addressRequestDTO){
        AddressResponseDTO addressResponseDTO = iAddressService.save(addressRequestDTO);
        if(addressResponseDTO!=null){
            return new ResponseEntity<>(addressResponseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> edit(@RequestBody AddressRequestDTO addressRequestDTO, @Param("id") Long id){
        AddressResponseDTO addressResponseDTO = iAddressService.edit(addressRequestDTO,id);
        if(addressResponseDTO!=null){
            return new ResponseEntity<>(addressResponseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<?> remove(@Param("id") Long id){
        if(iAddressService.remove(id)){
            return new ResponseEntity<>( HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@Param("id") Long id){
        AddressResponseDTO addressResponseDTO = iAddressService.findById(id);
        if(addressResponseDTO!=null){
            return new ResponseEntity<>(addressResponseDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/all")
    public ResponseEntity<?> findAll(){
        List<AddressResponseDTO> list = iAddressService.findAll();
        if(list!=null){
            return new ResponseEntity<>(list, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/pc/{postalCode}")
    public ResponseEntity<?> findByPostalCode(@Param("postalCode") String postalCode){
        List<AddressResponseDTO> list = iAddressService.findAddressesByPostalCode(postalCode);
        if(list!=null){
            return new ResponseEntity<>(list, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/find/cnt/{country}")
    public ResponseEntity<?> findByCountry(@Param("country") String country){
        List<AddressResponseDTO> list = iAddressService.findAddressesByCountry(country);
        if(list!=null){
            return new ResponseEntity<>(list, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
