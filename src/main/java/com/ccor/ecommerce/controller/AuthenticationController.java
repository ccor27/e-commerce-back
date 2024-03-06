package com.ccor.ecommerce.controller;

import com.ccor.ecommerce.exceptions.CustomerException;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.dto.AuthenticationRequestDTO;
import com.ccor.ecommerce.model.dto.AuthenticationResponseDTO;
import com.ccor.ecommerce.model.dto.CustomerRequestDTO;
import com.ccor.ecommerce.model.dto.CustomerResponseDTO;
import com.ccor.ecommerce.service.ICustomerService;
import com.ccor.ecommerce.service.registration.IRegistrationService;
import com.nimbusds.jose.jwk.OctetKeyPair;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private ICustomerService iCustomerService;
    private IRegistrationService iRegistrationService;
    @Autowired
    public AuthenticationController(ICustomerService iCustomerService, IRegistrationService iRegistrationService) {
        this.iCustomerService = iCustomerService;
        this.iRegistrationService = iRegistrationService;
    }

    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@RequestPart("requestDTO") CustomerRequestDTO requestDTO,
                                  @RequestPart("picture") MultipartFile picture)  {
        try{
            AuthenticationResponseDTO authenticationResponseDTO = iRegistrationService.save(requestDTO,picture);
            return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.CREATED);
        }catch (CustomerException ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO){
            AuthenticationResponseDTO responseDTO = iCustomerService.authenticate(authenticationRequestDTO);
            if(responseDTO!=null){
                return new ResponseEntity<>(responseDTO,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
   }
   @PostMapping("/authenticate/google")
   public void authenticateOAuth2Google(HttpServletResponse response) throws IOException {
       String googleOAuth2Url = "https://accounts.google.com/o/oauth2/v2/auth" +
               "?response_type=code" +
               "&client_id=246776448383-sh2l9734n7i47fkh8djg9s1o1u869agt.apps.googleusercontent.com" +
               "&scope=openid%20profile%20email" +
               "&state=MBcNuRF0tYoRufnz8O3dlrRaLGuPbS5kJVxUQHGkBKs%3D" +
               "&redirect_uri=http://localhost:8080/login/oauth2/code/google" +
               "&nonce=bqEDyUnj5k1X0WpZNC6WWseYqZI6KNMJ09sz2p6dQyk";

       // Redirect the user to the Google OAuth2 login page
       response.sendRedirect(googleOAuth2Url);
   }
   @GetMapping("/confirm")
   public  ResponseEntity<?> confirmToken(@RequestParam("token")String token){
        return new ResponseEntity<>(iRegistrationService.confirmToken(token),HttpStatus.OK);
   }
}
