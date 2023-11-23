package ru.anime.okami.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anime.okami.generatedPojo.List;
import ru.anime.okami.generatedPojo.Results;
import ru.anime.okami.generatedPojo.Translation;
import ru.anime.okami.repository.ResultsPaginationRepository;
import ru.anime.okami.repository.ResultsRepository;
import ru.anime.okami.service.impl.DataService;

import java.io.IOException;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DBController {

    private final DataService dataService;
    private final HttpHeaders responseHeaders = new HttpHeaders();

    {
        responseHeaders.setAccessControlAllowMethods(java.util.List.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.GET, HttpMethod.DELETE, HttpMethod.OPTIONS));
        responseHeaders.setAccessControlAllowHeaders(java.util.List.of("Origin", "X-Api-Key", "X-Requested-With", "Content-Type", "Accept", "Authorization"));
    }

    @GetMapping("/save")
    public ResponseEntity<List> saveData() throws IOException {
        return new ResponseEntity<>(dataService.save(), HttpStatus.OK);
    }

    @GetMapping("/update")
    public ResponseEntity<?> updateData() {
        return ResponseEntity.ok().headers(responseHeaders).body(dataService.update());
    }

    @GetMapping("/results")
    public ResponseEntity<java.util.List<Results>> getResults() {
        return new ResponseEntity<>(dataService.getResults(), HttpStatus.OK);
    }

    @GetMapping("/save-translations")
    public ResponseEntity<java.util.List<Translation>> saveTranslations() throws IOException {
        return new ResponseEntity<>(dataService.saveTranslations(), HttpStatus.OK);
    }

}
