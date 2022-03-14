package com.rs.depositoretiro.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientTemplate {

    public WebClient templateWebClient(String url){

        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
