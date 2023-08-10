package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.SaleException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
         throw new SaleException("The request to save is null");
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
            return new ArrayList<>();
        }
    }
    //TODO: maybe this method will be refactor
    @Override
    public SaleResponseDTO edit(SaleRequestDTO saleRequestDTO, Long id) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if(sale!=null && saleRequestDTO!=null){
                sale.setConcept(saleRequestDTO.concept());
                sale.setProductsSold(productsSold(saleRequestDTO.products()));
            return saleDTOMapper.apply(sale);
        }else{
          throw new SaleException("The sale fetched to update doesn't exist or the request is null");
        }
    }

    @Override
    public SaleResponseDTO findById(Long id) {
        if(saleRepository.existsById(id)){
            return saleDTOMapper.apply(saleRepository.findById(id).get());
        }else{
            throw new SaleException("The sale fetched by id doesn't exist");
        }
    }

    @Override
    public List<SaleResponseDTO> findAll(Integer offset, Integer pageSize) {
        Page<Sale> list = saleRepository.findAll(PageRequest.of(offset,pageSize));
        if(list!=null){
          return list.stream().map(sale -> {
              return saleDTOMapper.apply(sale);
          }).collect(Collectors.toList());
        }else{
            throw new SaleException("The list of sales is null");
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
            throw new SaleException("The sale fetched to add it a new product doesn't exist");
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
            throw new SaleException("The sale fetched or the product to delete doesn't exist");
        }
    }
  //TODO: implement a pageable
    @Override
    public List<ProductSoldResponseDTO> findProductsSold(Long id, Integer offset,Integer pageSize) {
        if(saleRepository.existsById(id)){
            Page<ProductSold> list = saleRepository.findSaleProductsSold(id,PageRequest.of(offset,pageSize));
            if(list!=null){
                return list.stream().map(productSold -> {
                    return productSoldDTOMapper.apply(productSold);
                }).collect(Collectors.toList());
            }else{
                throw new SaleException("The list of sale's product is null");
            }
        }else{
            throw new SaleException("The sale fetched to find its product doesn't exist");
        }
    }
}
