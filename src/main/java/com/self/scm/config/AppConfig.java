package com.self.scm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class AppConfig {

    @Value("${cloud.name}")
    private String cloudName;
    @Value("${cloud.api-key}")
    private String cloudApi;
    @Value("${cloud.api-secret}")
    private String cloudSecret;

    @Bean
    public Cloudinary cloudinary(){ 
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", cloudApi,
            "api_secret", cloudSecret
        ));
    }

}
