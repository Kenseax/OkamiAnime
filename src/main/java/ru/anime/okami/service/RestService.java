package ru.anime.okami.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getPostsPlainJSON() {
        String url = "https://kodikapi.com/list?token=9369488b540d3a88695d0407421b3197&types=anime%2Canime-serial&with_material_data=true&limit=50";
        return this.restTemplate.getForObject(url, String.class);
    }
}
