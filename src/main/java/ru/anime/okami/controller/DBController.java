package ru.anime.okami.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anime.okami.generatedPojo.List;
import ru.anime.okami.generatedPojo.Results;
import ru.anime.okami.generatedPojo.Translation;
import ru.anime.okami.repository.ResultsRepository;
import ru.anime.okami.service.impl.DataService;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "https://4298-79-139-249-160.ngrok-free.app/", maxAge = 648000, allowCredentials = "true")
@RequestMapping("/data")
public class DBController {

    private final DataService dataService;

    @Autowired
    private ResultsRepository resultsRepository;

    public DBController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/save")
    public ResponseEntity<List> saveData() throws IOException {
        return new ResponseEntity<List>(dataService.save(), HttpStatus.OK);
    }

    @GetMapping("/update")
    public ResponseEntity<?> updateData() {
        Results update = dataService.update();
        return new ResponseEntity<Results>(update, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ru.anime.okami.generatedPojo.List> getList() {
        return new ResponseEntity<ru.anime.okami.generatedPojo.List>(dataService.getList(), HttpStatus.OK);
    }

    @GetMapping("/results")
    public ResponseEntity<java.util.List<Results>> getResults() {
        return new ResponseEntity<java.util.List<Results>>(dataService.getResults(), HttpStatus.OK);
    }

    @GetMapping("/save-translations")
    public ResponseEntity<java.util.List<Translation>> saveTranslations() throws IOException {
        return new ResponseEntity<java.util.List<Translation>>(dataService.saveTranslations(), HttpStatus.OK);
    }

}
