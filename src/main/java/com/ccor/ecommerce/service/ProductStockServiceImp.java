package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.AddressException;
import com.ccor.ecommerce.exceptions.ProductStockException;
import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.model.dto.ProductStockRequestDTO;
import com.ccor.ecommerce.model.dto.ProductStockResponseDTO;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.repository.ProductStockRepository;
import com.ccor.ecommerce.service.mapper.ProductStockDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductStockServiceImp implements IProductStockService{
    @Autowired
    private ProductStockRepository productStockRepository;
    @Autowired
    private ProductStockDTOMapper productStockDTOMapper;
    @Autowired
    private INotificationService iNotificationService;
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public ProductStockResponseDTO save(ProductStockRequestDTO productStockRequestDTO,MultipartFile picture) {
        if(productStockRequestDTO!=null){
            if(validateNameAndBarCode(productStockRequestDTO.name(), productStockRequestDTO.barCode())){
                throw new ProductStockException("Already exist a product with that name or barcode.");
            }else{
                ProductStock productStock = ProductStock.builder()
                        .name(productStockRequestDTO.name())
                        .amount(productStockRequestDTO.amount())
                        .pricePerUnit(productStockRequestDTO.pricePerUnit())
                        .barCode(productStockRequestDTO.barCode())
                        .enableProduct(productStockRequestDTO.enableProduct())
                        .build();
                if(picture!=null){
                    try {
                        productStock.setPicturePath(convertImageToBase64Image(picture));
                    } catch (IOException e) {
                        throw new ProductStockException("Error saving the product image, "+e.getLocalizedMessage());
                    }
                }
                //send the notification to all the customers
                sendNotificationNewProductEmail(productStock.getName());
                sendNotificationNewProductSms(productStock.getName());
                return productStockDTOMapper.apply(productStockRepository.save(productStock));
            }
        }else{
            throw new ProductStockException("There not data, the request is null");
        }
    }
    private String convertImageToBase64Image(MultipartFile multipartFile) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    private void sendNotificationNewProductEmail(String  productStockName) {
        iNotificationService.notifyByEmailNewProduct(buildMessageNewProductByEmail(productStockName));
    }
    private void sendNotificationNewProductSms(String  productStockName){
        iNotificationService.notifyBySmsNewProduct(buildMessageNewProductBySms(productStockName));
    }
    private String buildMessageNewProductByEmail(String productName){
        return "<div style=\"font-family:Helvetica, Arial, sans-serif; font-size:16px; margin:0; color:#0b0c0c\">\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">Exciting news! We have a new product available: "+productName+" </p>\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">Check it out and explore the latest addition to our collection. We hope you find something you love!</p>\n" +
                "</div>";
    }
    private String buildMessageNewProductBySms(String productName){
        return "Exciting news! We have a new product available: "+productName+".\n" +
                "Check it out and explore the latest addition to our collection. We hope you find something you love!" ;
    }
    public boolean validateNameAndBarCode(String name, String barCode){
        return productStockRepository.existsByName(name) || productStockRepository.existsByBarCode(barCode);
    }
    @Override
    public boolean remove(Long id) {
        if(productStockRepository.existsById(id)){
            productStockRepository.deleteById(id);
            return true;
        }else{
           throw new ProductStockException("The product to delete doesn't exist");
        }
    }
    //TODO: might refactor it
    @Override
    public ProductStockResponseDTO edit(ProductStockRequestDTO productStockRequestDTO,MultipartFile picture, Long id) {
         ProductStock productStock = productStockRepository.findById(id).orElse(null);
        if(productStockRequestDTO!=null && productStock!=null){
            productStock.setName(productStockRequestDTO.name());
            productStock.setAmount(productStockRequestDTO.amount());
            productStock.setPricePerUnit(productStockRequestDTO.pricePerUnit());
            productStock.setBarCode(productStockRequestDTO.barCode());
            productStock.setEnableProduct(productStockRequestDTO.enableProduct());
            if(picture!=null){
                try {
                    productStock.setPicturePath(convertImageToBase64Image(picture));
                } catch (IOException e) {
                    throw new ProductStockException("Error saving the product image, "+e.getLocalizedMessage());
                }
            }else{
                productStock.setPicturePath(null);
            }

            return productStockDTOMapper.apply(productStockRepository.save(productStock));
        }else{
            throw new ProductStockException("The product to update doesn't exist or the request is null");
        }
    }

    @Override
    public List<ProductStockResponseDTO> findAll(Integer offset,Integer pageSize) {
        Page<ProductStock> list = productStockRepository.findAll(PageRequest.of(offset,pageSize));
        //List<ProductStock> list = productStockRepository.findAll();
        if(list!=null && !list.isEmpty()){

            int totalProducts = productStockRepository.countProductStock();
            int adjustedOffset = pageSize*offset;
            adjustedOffset = Math.min(adjustedOffset,totalProducts);
            if(adjustedOffset>=totalProducts){
                throw new AddressException("There aren't the enough products in stock");
            }else {
                return list.stream().map(productStock -> {
                    return productStockDTOMapper.apply(productStock);
                }).collect(Collectors.toList());
            }

        }else{
            throw new ProductStockException("The list of products is null");
        }
    }

    @Override
    public List<ProductStock> findAllToExport(Integer offset, Integer pageSize) {
        return productStockRepository.findAll(PageRequest.of(offset,pageSize)).getContent();
    }

    @Override
    public List<ProductStock> findAllToExport() {
        return productStockRepository.findAll();
    }

    @Override
    public ProductStockResponseDTO findProductById(Long id) {
        ProductStock productStock = productStockRepository.findById(id).orElse(null);
        if(productStock!=null){
            return productStockDTOMapper.apply(productStock);
        }else{
            throw new ProductStockException("The product searched doesn't exist");
        }
    }

    @Override
    public List<ProductStockResponseDTO> findProductStocksByEnableProduct(Integer offset, Integer pageSize) {
        int totalProducts = productStockRepository.countByEnableProduct();
        int adjustedOffset = pageSize*offset;
        adjustedOffset = Math.min(adjustedOffset,totalProducts);
        if(adjustedOffset>=totalProducts){
            throw new ProductStockException("There aren't the enough products");
        }else{
            Page<ProductStock> list =productStockRepository.findProductStocksByEnableProduct(PageRequest.of(offset,pageSize));
            if(list!=null){
                return list.stream().map(productStock -> {
                    return productStockDTOMapper.apply(productStock);
                }).collect(Collectors.toList());
            }else{
                throw new ProductStockException("The list of products is null");
            }
        }

    }

    @Override
    public ProductStockResponseDTO findProductStockByBarCode(String barCode) {
        ProductStock productStock = productStockRepository.findProductStockByBarCode(barCode).orElse(null);
        if(productStock!=null){
            return productStockDTOMapper.apply(productStock);
        }else{
            throw new ProductStockException("The product searched by barcode doesn't exit");
        }
    }

    @Override
    //TODO: refactor this to use the availableAmount from the repository
    public ProductStockResponseDTO sellProduct(int amountSold, String barCode) {
        ProductStock productStock = productStockRepository.amountIsAvailable(barCode,amountSold).orElse(null);
        if(productStock!=null){
                int stockAmount = productStock.getAmount();
                productStock.setAmount(stockAmount-amountSold);
                productStock.setEnableProduct(productStock.getAmount()>0 ? true:false);
                return productStockDTOMapper.apply(productStockRepository.save(productStock));
        }else{
            throw new ProductStockException("The product to sell doesn't exist or The are not the amount requested");
        }
    }

    @Override
    public void addBackAmount(String barcode, int amount) {
      ProductStock p = productStockRepository.findProductStockByBarCode(barcode).orElse(null);
      if(p!=null){
          p.setAmount(p.getAmount()+amount);
          productStockRepository.save(p);
      }else {
          throw new ProductStockException("The product with the barcode: "+barcode+" doesn't exist, " +
                  "therefore is not possible add the amount back");
      }
    }
}
