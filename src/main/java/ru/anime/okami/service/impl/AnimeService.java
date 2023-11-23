package ru.anime.okami.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.anime.okami.generatedPojo.Results;
import ru.anime.okami.repository.ResultsPaginationRepository;

import java.util.ArrayList;

@Service
public class AnimeService {

    private final ResultsPaginationRepository resultsRepository;


    @Autowired
    public AnimeService(ResultsPaginationRepository resultsRepository) {
        this.resultsRepository = resultsRepository;
    }

    @Value("${kodik.token}")
    private String TOKEN;

    public Page<Results> search(String request, int offset, int limit) {
        return resultsRepository.findAllByTitleContainsIgnoreCase(request, PageRequest.of(offset, limit)).orElse(null);
    }
}
