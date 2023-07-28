package com.ccor.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration1 {
    @Bean
    public WebMvcConfigurer corsConfiguration(){
        return new WebMvcConfigurer() {
         @Override
         public void addCorsMappings(CorsRegistry registry){
             registry.addMapping("/**");
         }
        };
    }
}
