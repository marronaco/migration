package com.eviden.migration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient drupalWebClient(){
        return WebClient.builder()
                .baseUrl("http://localhost/test_drupal/rest")
                .build();
    }

    @Bean
    public WebClient magentoWebClient(){
        return WebClient.builder()
                .baseUrl("http://marronaco.magento2.com/rest/V1")
                .build();

    }
}
