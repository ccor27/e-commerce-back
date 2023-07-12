package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.model.dto.ProductStockRequestDTO;
import com.ccor.ecommerce.model.dto.ProductStockResponseDTO;
import com.ccor.ecommerce.repository.ProductStockRepository;
import com.ccor.ecommerce.service.mapper.ProductStockDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductStockServiceImp implements IProductStockService{
    @Autowired
    private ProductStockRepository productStockRepository;
    @Autowired
    private ProductStockDTOMapper productStockDTOMapper;
    @Override
    public ProductStockResponseDTO save(ProductStockRequestDTO productStockRequestDTO) {
        if(productStockRequestDTO!=null){
            ProductStock productStock = ProductStock.builder()
                    .name(productStockRequestDTO.name())
                    .amount(productStockRequestDTO.amount())
                    .pricePerUnit(productStockRequestDTO.pricePerUnit())
                    .barCode(productStockRequestDTO.barCode())
                    .enableProduct(productStockRequestDTO.enableProduct())
                    .build();
            return productStockDTOMapper.apply(productStockRepository.save(productStock));
        }else{
           return null;
        }
    }

    @Override
    public boolean remove(Long id) {
        if(productStockRepository.existsById(id)){
            productStockRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ProductStockResponseDTO edit(ProductStockRequestDTO productStockRequestDTO, Long id) {
         ProductStock productStock = productStockRepository.findById(id).orElse(null);
        if(productStockRequestDTO!=null && productStock!=null){
            productStock.setName(productStockRequestDTO.name());
            productStock.setAmount(productStockRequestDTO.amount());
            productStock.setPricePerUnit(productStockRequestDTO.pricePerUnit());
            productStock.setBarCode(productStockRequestDTO.barCode());
            productStock.setEnableProduct(productStockRequestDTO.enableProduct());
            return productStockDTOMapper.apply(productStockRepository.save(productStock));
        }else{
            return null;
        }
    }

    @Override
    public List<ProductStockResponseDTO> findProductStocksByEnableProduct() {
        List<ProductStock> list =productStockRepository.findProductStocksByEnableProduct();
        if(list!=null && !list.isEmpty()){
            return list.stream().map(productStock -> {
                return productStockDTOMapper.apply(productStock);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    public ProductStockResponseDTO findProductStocksByBarCode(String barCode) {
        ProductStock productStock = productStockRepository.findProductStocksByBarCode(barCode).orElse(null);
        if(productStock!=null){
            return productStockDTOMapper.apply(productStock);
        }else{
            return null;
        }
    }

    @Override
    public ProductStockResponseDTO sellProduct(int amountSold, Long id) {
        ProductStock productStock = productStockRepository.findById(id).orElse(null);
        if(productStock!=null){
            int stockAmount = productStock.getAmount();
            if(stockAmount>=amountSold){
                productStock.setAmount(stockAmount-amountSold);
                productStock.setEnableProduct(productStock.getAmount()>0 ? true:false);
                return productStockDTOMapper.apply(productStockRepository.save(productStock));
            }else{
                return null;
            }

        }else{
            return null;
        }
    }
}
