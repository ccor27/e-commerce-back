package com.ccor.ecommerce.config;

import com.ccor.ecommerce.controller.AuthenticationController;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.Token;
import com.ccor.ecommerce.model.TokenType;
import com.ccor.ecommerce.model.dto.AuthenticationRequestDTO;
import com.ccor.ecommerce.model.dto.AuthenticationResponseDTO;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.repository.TokenRepository;
import com.ccor.ecommerce.service.ICustomerService;
import com.ccor.ecommerce.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@Configuration
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenRepository tokenRepository;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if(authentication instanceof OAuth2AuthenticationToken){
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();
            Customer customer = customerRepository.
                    findCustomerByEmail(oauth2User.getAttribute("email").toString()).orElse(null);
            if(customer!=null){
                System.out.println("customer exist");
                String token = jwtService.generateToken(customer);
                saveCustomerToken(customer,token);
                response.getWriter().write(token);
                response.getWriter().flush();

            }else{
                //TODO: redirect a error endpoint because the customer doesn't exist or create new one
            }
        }else{
            //error
        }

       }
    private void saveCustomerToken (Customer customer, String jwtToken){
        Token token = new Token(
                null,
                jwtToken,
                TokenType.BEARER,
                customer,
                false,
                false
        );
        tokenRepository.save(token);
    }
    }

