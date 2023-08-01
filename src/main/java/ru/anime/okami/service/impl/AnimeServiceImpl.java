package ru.anime.okami.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.anime.okami.model.Anime;
import ru.anime.okami.payload.PostDto;
import ru.anime.okami.payload.PostResponse;
import ru.anime.okami.repository.AnimeRepository;
import ru.anime.okami.service.AnimeService;

import java.util.List;

@Service
public class AnimeServiceImpl implements AnimeService {

    private final AnimeRepository animeRepository;

    @Autowired
    public AnimeServiceImpl(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }


    @Override
    public Anime createAnime(Anime anime) {
        return null;
    }

    @Override
    public List<Anime> getAllAnime() {
        return null;
    }

    @Override
    public Anime getAnimeById(long id) {
        return null;
    }

    @Override
    public Anime updateAnime(Anime anime, long id) {
        return null;
    }

    @Override
    public void deleteAnimeById(long id) {

    }

    @Override
    public List<Anime> getAnimeByCategory(long categoryId) {
        return null;
    }
}
