package ru.anime.okami.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.anime.okami.generatedPojo.List;
import ru.anime.okami.generatedPojo.Results;
import ru.anime.okami.generatedPojo.Translation;
import ru.anime.okami.repository.ListRepository;
import ru.anime.okami.repository.MaterialDataRepository;
import ru.anime.okami.repository.ResultsRepository;
import ru.anime.okami.repository.TranslationRepository;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

@Service
public class DataService {


    private final ListRepository listRepository;
    private final ResultsRepository resultsRepository;
    private final MaterialDataRepository materialDataRepository;
    private final TranslationRepository translationRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kodik.token}")
    private String TOKEN;

    @Autowired
    public DataService(ListRepository listRepository, ResultsRepository resultsRepository, MaterialDataRepository materialDataRepository, TranslationRepository translationRepository) {
        this.listRepository = listRepository;
        this.resultsRepository = resultsRepository;
        this.materialDataRepository = materialDataRepository;
        this.translationRepository = translationRepository;
    }

    public List save() throws IOException {
        String resourceUrl = "https://kodikapi.com/list?token=" + TOKEN + "&types=anime%2Canime-serial&with_material_data=true&limit=50";
        List response;
        do {
            resourceUrl = resourceUrl.replaceFirst("%2C", ",");
            response = restTemplate.getForObject(resourceUrl, List.class);
            assert response != null;
            java.util.List<Results> results = response.getResults();
            java.util.List<Results> resultsWithoutDuplicate = new LinkedList<>();

            for (Results r : results) {
                if (translationRepository.findByTitle(r.getTranslation().getTitle()).isEmpty()) {
                    translationRepository.save(r.getTranslation());
                }
                if (resultsWithoutDuplicate.stream().anyMatch(o -> o.getTitle().equals(r.getTitle()))) {
                    Results resultsFromDuplicateList = resultsWithoutDuplicate.stream()
                            .filter(o -> o.getTitle().equals(r.getTitle()))
                            .findFirst().orElse(null);
                    java.util.List<String> allTranslations = new LinkedList<>(resultsFromDuplicateList.getAllTranslations());

                    allTranslations.add(r.getTranslation().getTitle());

                    resultsFromDuplicateList.setAllTranslations(allTranslations);

                    resultsWithoutDuplicate.add(resultsFromDuplicateList);
                } else if (resultsRepository.findByTitle(r.getTitle()).isPresent()) {
                    Results resultsFromDb = resultsRepository.findByTitle(r.getTitle()).orElse(null);
                    java.util.List<String> allTranslations = new LinkedList<>(resultsFromDb.getAllTranslations());
                    allTranslations.add(r.getTranslation().getTitle());
                    resultsFromDb.setAllTranslations(allTranslations);
                    resultsRepository.save(resultsFromDb);
                } else {
                    r.setAllTranslations(java.util.List.of(r.getTranslation().getTitle()));
                    resultsWithoutDuplicate.add(r);

                    if (r.getMaterialData() != null) {
                        r.setMaterialData(r.getMaterialData());
                        materialDataRepository.save(r.getMaterialData());
                    }
                }
            }

            response.setResults(resultsWithoutDuplicate);
            resultsRepository.saveAll(resultsWithoutDuplicate);
            listRepository.save(response);

            resourceUrl = response.getNextPage();
        } while (response.getNextPage() != null);

        return response;
    }


    public java.util.List<Results> update() {
        String resourceUrl = "https://kodikapi.com/list?token=" + TOKEN + "&types=anime,anime-serial&with_material_data=true&limit=100";
        List response = restTemplate.getForObject(resourceUrl, List.class);
        java.util.List<Results> results = response.getResults();
        java.util.List<Results> updatedResults = new LinkedList<>();

        for (Results r : results) {
            Results resFromDb = resultsRepository.findByTitle(r.getTitle()).orElse(null);
            if (resFromDb != null) {
                java.util.List<String> translations = resFromDb.getAllTranslations();
                if (!translations.contains(r.getTranslation().getTitle())) {
                    translations.add(r.getTranslation().getTitle());
                }
                resultsRepository.save(r);
                updatedResults.add(r);
            }
        }

        return updatedResults;
    }

    public List getList() {
        return listRepository.findById(13L).orElse(null);
    }

    public java.util.List<Results> getResults() {
        return resultsRepository.findAll();
    }

    public java.util.List<Translation> saveTranslations() throws IOException {
        File file = new File("src/main/java/ru/anime/okami/schema/required.json");
        ObjectMapper objectMapper = new ObjectMapper();

        Translation[] translations = objectMapper.readValue(file, Translation[].class);

        if (translations != null) {
            translationRepository.saveAll(java.util.List.of(translations));
        }

        return translationRepository.findAll();
    }
}
