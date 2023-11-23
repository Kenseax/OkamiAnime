package ru.anime.okami.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anime.okami.generatedPojo.Results;
import ru.anime.okami.repository.ResultsPaginationRepository;
import ru.anime.okami.repository.ResultsRepository;
import ru.anime.okami.service.impl.AnimeService;

import java.util.List;

@RestController
@RequestMapping("/anime")
public class AnimeController {


    private final ResultsPaginationRepository paginationRepository;
    private final ResultsRepository resultsRepository;
    private final AnimeService animeService;

    @Autowired
    public AnimeController(ResultsPaginationRepository paginationRepository, ResultsRepository resultsRepository, AnimeService animeService) {
        this.paginationRepository = paginationRepository;
        this.resultsRepository = resultsRepository;
        this.animeService = animeService;
    }

    @GetMapping("/list/page={offset}&limit={limit}")
    public ResponseEntity<Page<Results>> getList(@PathVariable("offset") Integer offset,
                                                 @PathVariable(value = "limit") @Min(6) @Max(18) Integer limit,
                                                 HttpServletResponse response) {
        Page<Results> page = paginationRepository.findAll(PageRequest.of(offset, limit, Sort.by("updatedAt").descending()));
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Results> getAnimeById(@PathVariable String id) {
        return ResponseEntity.ok(resultsRepository.findById(id).orElse(null));
    }

    @GetMapping("/search/title={title}&page={page}")
    public ResponseEntity<Page<Results>> search(@PathVariable("title") String title,
                                                @PathVariable(value = "page", required = false) Integer page) {
        if (page == null) {
            page = 0;
        }
        return ResponseEntity.ok(animeService.search(title, page, 18));
    }
}
