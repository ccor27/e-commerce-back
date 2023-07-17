package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.model.dto.SaleRequestDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import com.ccor.ecommerce.repository.ProductSoldRepository;
import com.ccor.ecommerce.repository.SaleRepository;
import com.ccor.ecommerce.service.mapper.ProductSoldDTOMapper;
import com.ccor.ecommerce.service.mapper.SaleDTOMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImp implements ISaleService{
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ProductSoldRepository productSoldRepository;
    @Autowired
    private SaleDTOMapper saleDTOMapper;
    @Autowired
    private ProductSoldDTOMapper productSoldDTOMapper;
    @Override
    public SaleResponseDTO save(SaleRequestDTO saleRequestDTO) {
        if(saleRequestDTO!=null){
            Sale sale;
            if(saleRequestDTO.products()!=null){
                sale=Sale.builder()
                        .concept(saleRequestDTO.concept())
                        .productsSold(productsSold(saleRequestDTO.products()))
                        .createAt(new Date())
                        .build();
            }else{
                sale=Sale.builder()
                        .concept(saleRequestDTO.concept())
                        .productsSold(new ArrayList<>())
                        .createAt(new Date())
                        .build();
            }
            return saleDTOMapper.apply(saleRepository.save(sale));
        }else{
         return null;
        }
    }

    private List<ProductSold> productsSold(List<ProductSoldRequestDTO> list){
        if(list!=null){
            return list.stream().map(productSoldRequestDTO -> {
                return new ProductSold(
                        null,
                        productSoldRequestDTO.barCode(),
                        productSoldRequestDTO.name(),
                        productSoldRequestDTO.amount(),
                        productSoldRequestDTO.price()
                );
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }
    @Override
    public SaleResponseDTO edit(SaleRequestDTO saleRequestDTO, Long id) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if(sale!=null && saleRequestDTO!=null){
            if(saleRequestDTO.products()!=null){
               sale.setConcept(saleRequestDTO.concept());
               sale.setProductsSold(null);
            }else{
                sale.setConcept(saleRequestDTO.concept());
                sale.setProductsSold(productsSold(saleRequestDTO.products()));
            }
            return saleDTOMapper.apply(sale);
        }else{
          return null;
        }
    }

    @Override
    public SaleResponseDTO findById(Long id) {
        if(saleRepository.existsById(id)){
            return saleDTOMapper.apply(saleRepository.findById(id).get());
        }else{
            return null;
        }
    }

    @Override
    public List<SaleResponseDTO> findAll() {
        List<Sale> list = saleRepository.findAll();
        if(list!=null && list.size()>0){
          return list.stream().map(sale -> {
              return saleDTOMapper.apply(sale);
          }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public SaleResponseDTO addProductSold(ProductSoldRequestDTO productSoldRequesDTO, Long id) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if(sale!=null){
            ProductSold productSold = new ProductSold(
                    null,
                    productSoldRequesDTO.barCode(),
                    productSoldRequesDTO.name(),
                    productSoldRequesDTO.amount(),
                    productSoldRequesDTO.price());
            sale.getProductsSold().add(productSold);
            return saleDTOMapper.apply(saleRepository.save(sale));
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public SaleResponseDTO removeProductSold(Long id_product, Long id_sale) {
        Sale sale = saleRepository.findById(id_sale).orElse(null);
        ProductSold productSold = productSoldRepository.findById(id_product).orElse(null);
        if(sale!=null && productSold!=null){
            sale.getProductsSold().remove(productSold);
            return saleDTOMapper.apply(saleRepository.save(sale));
        }else{
            return null;
        }
    }

    @Override
    public List<ProductSoldResponseDTO> findProductsSold(Long id) {
        if(saleRepository.existsById(id)){
            List<ProductSold> list = saleRepository.findSaleProductsSold(id);
            if(list!=null && !list.isEmpty()){
                return list.stream().map(productSold -> {
                    return productSoldDTOMapper.apply(productSold);
                }).collect(Collectors.toList());
            }else{
                System.out.println("else de lilst");
                return null;
            }
        }else{
            return null;
        }
    }
}
