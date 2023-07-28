package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.repository.ProductSoldRepository;
import com.ccor.ecommerce.service.mapper.ProductSoldDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSoldServiceImp implements IProductSoldService{
    @Autowired
    private ProductSoldRepository productSoldRepository;
    @Autowired
    private ProductSoldDTOMapper productSoldDTOMapper;
    @Override
    public ProductSoldResponseDTO save(ProductSoldRequestDTO productSoldRequestDTO) {
        if(productSoldRequestDTO!=null){
            ProductSold sold = ProductSold.builder()
                    .barCode(productSoldRequestDTO.barCode())
                    .name(productSoldRequestDTO.name())
                    .amount(productSoldRequestDTO.amount())
                    .price(productSoldRequestDTO.price())
                    .build();
            return productSoldDTOMapper.apply(productSoldRepository.save(sold));
        }else{
            return null;
        }
    }

    @Override
    public ProductSoldResponseDTO edit(ProductSoldRequestDTO productSoldRequestDTO, Long id) {
        ProductSold sold = productSoldRepository.findById(id).orElse(null);
        if(sold!=null){
            sold.setName(productSoldRequestDTO.name());
            sold.setBarCode(productSoldRequestDTO.barCode());
            sold.setAmount(productSoldRequestDTO.amount());
            sold.setPrice(productSoldRequestDTO.price());
            return productSoldDTOMapper.apply(productSoldRepository.save(sold));
        }else{
            return null;
        }

    }

    @Override
    public boolean remove(Long id) {
        if(productSoldRepository.existsById(id)){
            productSoldRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ProductSoldResponseDTO findById(Long id) {
        ProductSold productSold = productSoldRepository.findById(id).orElse(null);
        if(productSold!=null){
            return productSoldDTOMapper.apply(productSold);
        }else{
            return null;
        }
    }

    @Override
    public List<ProductSoldResponseDTO> findAll() {
        List<ProductSold> list = productSoldRepository.findAll();
        if(list!=null){
          return list.stream().map(productSold -> {
              return productSoldDTOMapper.apply(productSold);
          }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    public List<ProductSoldResponseDTO> findProductsSoldByBarCode(String barcode) {
        List<ProductSold> list = productSoldRepository.findProductsSoldByBarCode(barcode);
        if(list!=null &&!list.isEmpty()){
            return list.stream().map(productSold -> {
                return productSoldDTOMapper.apply(productSold);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }
}
