package com.Lyon_FindR.backend.client;


import com.Lyon_FindR.backend.dto.OverpassResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OverpassClient {

    private final RestClient restClient;

    public OverpassClient(RestClient overpassRestClient) {
        this.restClient = overpassRestClient;
    }

    public OverpassResponse search(String query) {
        return restClient.post()
                .uri("/interpreter")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("data=" + URLEncoder.encode(query, StandardCharsets.UTF_8))
                .retrieve()
                .body(OverpassResponse.class);
    }
}
