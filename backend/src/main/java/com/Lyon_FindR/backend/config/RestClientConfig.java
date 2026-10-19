package com.Lyon_FindR.backend.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;


@Configuration
public class RestClientConfig {

    @Bean
    RestClient overpassRestClient(@Value("${osm.overpass.url}") String url,
                                  @Value("${osm.user-agent}") String userAgent) {
        HttpClient http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(http);
        factory.setReadTimeout(Duration.ofSeconds(120));

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl(url)
                .defaultHeader("User-Agent", userAgent)
                .build();
    }
}

