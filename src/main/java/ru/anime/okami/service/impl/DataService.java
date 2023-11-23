package ru.anime.okami.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.anime.okami.generatedPojo.List;
import ru.anime.okami.generatedPojo.Results;
import ru.anime.okami.generatedPojo.Translation;
import ru.anime.okami.repository.ListRepository;
import ru.anime.okami.repository.MaterialDataRepository;
import ru.anime.okami.repository.ResultsRepository;
import ru.anime.okami.repository.TranslationRepository;

import java.io.IOException;
import java.util.LinkedList;

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
        String resourceUrl = "https://kodikapi.com/list?token=" + TOKEN + "&types=anime%2Canime-serial&with_material_data=true&limit=50&not_blocked_in=RU";
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


    @Transactional
    public java.util.List<Results> update() {
        String resourceUrl = "https://kodikapi.com/list?token=" + TOKEN + "&types=anime,anime-serial&with_material_data=true&limit=100";
        List response = restTemplate.getForObject(resourceUrl, List.class);
        java.util.List<Results> results = response.getResults();
        java.util.List<Results> updatedResults = new LinkedList<>();

        for (Results r : results) {
            if (translationRepository.findByTitle(r.getTranslation().getTitle()).isEmpty()) {
                translationRepository.save(r.getTranslation());
            }
            Results resFromDb = resultsRepository.findFirstByTitle(r.getTitle()).orElse(null);
            if (resFromDb == null) {
                r.setAllTranslations(java.util.List.of(r.getTranslation().getTitle()));
                if (r.getMaterialData() != null) {
                    materialDataRepository.save(r.getMaterialData());
                }
                resultsRepository.save(r);
                updatedResults.add(r);
            } else if (resFromDb.getId().equals(r.getId()) &&
                    (resFromDb.getEpisodesCount() < r.getEpisodesCount() || resFromDb.getLastEpisode() < r.getLastEpisode())) {

                resFromDb.setUpdatedAt(r.getUpdatedAt());
                if (resFromDb.getEpisodesCount() < r.getEpisodesCount()) {
                    resFromDb.setEpisodesCount(r.getEpisodesCount());
                }
                if (resFromDb.getLastEpisode() < r.getLastEpisode()) {
                    resFromDb.setLastEpisode(r.getLastEpisode());
                }

                resultsRepository.save(resFromDb);
                updatedResults.add(resFromDb);
            } else if (!resFromDb.getAllTranslations().contains(r.getTranslation().getTitle())) {
                java.util.List<String> translations = new LinkedList<>(resFromDb.getAllTranslations());
                translations.add(r.getTranslation().getTitle());
                resFromDb.setUpdatedAt(r.getUpdatedAt());
                resFromDb.setAllTranslations(translations);
                resultsRepository.save(resFromDb);
                updatedResults.add(resFromDb);
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
        String resourceUrl = "https://kodikapi.com/list?token=" + TOKEN;

        Translation[] translations = restTemplate.getForObject(resourceUrl, Translation[].class);

        if (translations != null) {
            translationRepository.saveAll(java.util.List.of(translations));
        }

        return translationRepository.findAll();
    }
}
