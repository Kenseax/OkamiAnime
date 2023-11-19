package ru.anime.okami;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "https://kodikapi.com/list?token=9369488b540d3a88695d0407421b3197&types=anime,anime-serial&with_material_data=true&limit=1";
        ResponseEntity<String> response;

        //restTemplate.headForHeaders(resourceUrl).set(HttpHeaders.CONTENT_TYPE, "application/json");

        response = restTemplate.getForEntity(resourceUrl, String.class);

        System.out.println(response);

    }
}
