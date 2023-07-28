package com.ccor.ecommerce.config;

import com.ccor.ecommerce.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static com.ccor.ecommerce.model.Role.ADMIN;
import static com.ccor.ecommerce.model.Role.CUSTOMER;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private LogoutService logoutService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String[] adminUrls =
                        {"/api/v1/address/**",
                         "/api/v1/customer/**",
                         "/api/v1/card/**",
                         "/api/v1/history/**",
                         "/api/v1/productSold/**",
                         "/api/v1/productStock/**",
                         "/api/v1/sale/**"};
    private static final String[] customersUrls =
                        {"/api/v1/customer/find/{id}",
                         "/api/v1/customer/{id}/edit",
                         "/api/v1/customer/{id}/add/address",
                         "/api/v1/customer/{id_customer}/remove/address/{id_address}",
                         "/api/v1/customer/{id}/add/card",
                         "/api/v1/customer/{id_customer}/remove/card/{id_card}",
                         "/api/v1/customer/find/{id}/history",
                         "/api/v1/history/{id}/add/sale",
                         "/api/v1/sale/save",
                         "/api/v1/productStock/find",
                         "/api/v1/productStock/find/{id}",
                         "/api/v1/customer/{id}/change/pwd/{pwd}",
                         "/api/v1/customer/find/by/tk/{token}"};
    private static final String[] openUrls=
                        {
                                "/api/v1/authentication/save",
                                "/api/v1/authentication/authenticate",
                                "/h2-console/**"
                         };

    /**
     * Method where the program allow some urls without needed to authentication.
     * Also, here configure some classes that will manage the authentication, filters and logout
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http

                .cors(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    auth
                            .requestMatchers( openUrls).permitAll()
                          //.requestMatchers(adminUrls).hasAnyRole(ADMIN.name())
                          //.requestMatchers(customersUrls).hasRole(CUSTOMER.name())
                            .anyRequest().authenticated();
                })
                .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout->{
                   logout.logoutUrl("/api/v1/auth/logout")
                            .addLogoutHandler(logoutService)
                            .logoutSuccessHandler(
                                    (request, response, authentication) ->
                                            SecurityContextHolder.clearContext());
                });

        return http.build();

    }

}
