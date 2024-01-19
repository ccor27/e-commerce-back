package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.AddressException;
import com.ccor.ecommerce.exceptions.CreditCardException;
import com.ccor.ecommerce.model.CreditCard;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.TypeCard;
import com.ccor.ecommerce.model.dto.CreditCardEditRequestDTO;
import com.ccor.ecommerce.model.dto.CreditCardRequestDTO;
import com.ccor.ecommerce.model.dto.CreditCardResponseDTO;
import com.ccor.ecommerce.repository.CreditCardRepository;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.service.mapper.CreditCardDTOMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditCardServiceImp implements ICreditCardService{
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CreditCardDTOMapper dtoMapper;

    @Override
    public CreditCardResponseDTO save(CreditCardRequestDTO requestDTO) {
        if(requestDTO!=null){
            try{
                CreditCard creditCard = CreditCard.builder()
                        .number(requestDTO.number())
                        .holderName(requestDTO.holderName())
                        .cvv(requestDTO.cvv())
                        .typeCard(typeCard(requestDTO.type()))
                        .monthExp(requestDTO.monthExp())
                        .yearExp(requestDTO.yearExp())
                        .build();
                return dtoMapper.apply(creditCardRepository.save(creditCard));
            }catch (Exception e){
                throw new CreditCardException("The credit card to save has something wrong - "+e.getLocalizedMessage());
            }
        }else{
        throw new CreditCardException("The credit card to save is null");
        }
    }
    private TypeCard typeCard(String type){
        return switch (type) {
            case "VISA" -> TypeCard.VISA;
            case "MASTER_CARD" -> TypeCard.MASTER_CARD;
            case "AMERICAN_EXPRESS" -> TypeCard.AMERICAN_EXPRESS;
            default -> null;
        };
    }

    @Override
    public CreditCardResponseDTO edit(CreditCardEditRequestDTO requestDTO, Long id) {
        CreditCard card = creditCardRepository.findById(id).orElse(null);
        if(card!=null && requestDTO!=null){
            card.setNumber(requestDTO.number());
            card.setHolderName(requestDTO.holderName());
            card.setCvv(requestDTO.cvv());
            card.setTypeCard(typeCard(requestDTO.type()));
            card.setMonthExp(requestDTO.monthExp());
            card.setYearExp(requestDTO.yearExp());
            return dtoMapper.apply(creditCardRepository.save(card));
        }else{
            throw new CreditCardException("The card to update doesn't exist or the request is null");
        }
    }

    @Override
    public CreditCardResponseDTO findById(Long id) {
        CreditCard card = creditCardRepository.findById(id).orElse(null);
        if(card!=null){
            return dtoMapper.apply(card);
        }else{
            throw new CreditCardException("The card fetched by id doesn't exist");
        }
    }

    @Override
    public CreditCardResponseDTO findCardByNumber(String number) {
        CreditCard card = creditCardRepository.findCreditCardByNumber(number).orElse(null);
        if (card != null){
            return dtoMapper.apply(card);
        }else{
         throw new CreditCardException("The credit fetched by number doesn't exist");
        }
    }

    @Override
    public List<CreditCardResponseDTO> findAll(Integer offset, Integer pageSize) {
        Page<CreditCard> cards = creditCardRepository.findAll(PageRequest.of(offset,pageSize));
        if(cards!=null && !cards.isEmpty()){

            int totalCards = creditCardRepository.countCreditCards();
            int adjustedOffset = pageSize*offset;
            adjustedOffset = Math.min(adjustedOffset,totalCards);
            if(adjustedOffset>=totalCards){
                throw new AddressException("There aren't the enough addresses");
            }else {
                return cards.getContent().stream().map(creditCard -> {
                    return dtoMapper.apply(creditCard);
                }).collect(Collectors.toList());
            }
        }else{
            throw new CreditCardException("The list of card is null");
        }
    }

    @Override
    public List<CreditCardResponseDTO> findCardsByType(Integer offset, Integer pageSize,String type) {
        Page<CreditCard> cards = creditCardRepository.findCreditCardsByTypeCard(PageRequest.of(offset,pageSize),typeCard(type));
        if(cards!=null){
            return cards.getContent().stream().map(creditCard -> {
                return dtoMapper.apply(creditCard);
            }).collect(Collectors.toList());
        }else{
            throw new CreditCardException("The list of card is null");
        }
    }
    @Transactional
    @Override
    public boolean remove(Long id) {
        CreditCard card = creditCardRepository.findById(id).orElse(null);
        if(card!=null){
            List<Customer> listCustomers = customerRepository.findCustomersByCardId(id);
            for (Customer customer:listCustomers ) {
                customer.getCards().remove(card);
                customerRepository.save(customer);
            }
            creditCardRepository.deleteById(id);
            return true;
        }else{
            throw new CreditCardException("The card fetched to delete doesn't exist");
        }
    }
}
