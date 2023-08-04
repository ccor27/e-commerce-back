package com.ccor.ecommerce.service.registration;

import com.ccor.ecommerce.model.ConfirmationToken;
import com.ccor.ecommerce.repository.ConfirmationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenServiceImp implements IConfirmationToken{
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Transactional
    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
       ConfirmationToken c = confirmationTokenRepository.save(token);
    }
    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }
    public void setConfirmedAt(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElse(null);
        if(confirmationToken==null) {
          throw new IllegalStateException("The token doesn't exist");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }
}