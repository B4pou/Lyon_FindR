package com.Lyon_FindR.backend.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;



@Configuration
public class RestClientConfig {

    @Bean
    RestClient overpassRestClient(@Value("${osm.overpass.url}") String url,
                                  @Value("${osm.user-agent}") String userAgent) {
        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader("User-Agent", userAgent)
                .build();
    }
}


