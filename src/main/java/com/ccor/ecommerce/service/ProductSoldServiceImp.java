package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.AddressException;
import com.ccor.ecommerce.exceptions.ProductSoldException;
import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.repository.ProductSoldRepository;
import com.ccor.ecommerce.service.mapper.ProductSoldDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
            throw new ProductSoldException("The request to save is null");
        }
    }
    @Override
    public ProductSoldResponseDTO findById(Long id) {
        ProductSold productSold = productSoldRepository.findById(id).orElse(null);
        if(productSold!=null){
            return productSoldDTOMapper.apply(productSold);
        }else{
            throw new ProductSoldException("The product sold fetched by id doesn't exist");
        }
    }

    @Override
    public List<ProductSoldResponseDTO> findAll(Integer offset, Integer pageSize) {
        Page<ProductSold> list = productSoldRepository.findAll(PageRequest.of(offset,pageSize));
        if(list!=null && !list.isEmpty()){

            int totalHistory = productSoldRepository.countProductsSold();
            int adjustedOffset = pageSize*offset;
            adjustedOffset = Math.min(adjustedOffset,totalHistory);
            if(adjustedOffset>=totalHistory){
                throw new AddressException("There aren't the enough products");
            }else {
                return list.stream().map(productSold -> {
                    return productSoldDTOMapper.apply(productSold);
                }).collect(Collectors.toList());
            }

        }else{
            throw new ProductSoldException("The list of products sold is null");
        }
    }

    @Override
    public List<ProductSold> findAllToExport(Integer offset, Integer pageSize) {
        return productSoldRepository.findAll(PageRequest.of(offset,pageSize)).getContent();
    }

    @Override
    public List<ProductSold> findAllToExport() {
        return productSoldRepository.findAll();
    }

    @Override
    public List<ProductSoldResponseDTO> findProductsSoldByBarCode(String barcode, Integer offset, Integer pageSize) {
        Page<ProductSold> list = productSoldRepository.findProductsSoldByBarCode(barcode,PageRequest.of(offset,pageSize));
        if(list!=null){
            return list.stream().map(productSold -> {
                return productSoldDTOMapper.apply(productSold);
            }).collect(Collectors.toList());
        }else{
            throw new ProductSoldException("The list of product fetched by barcode is null");
        }
    }
}
