package ru.anime.okami.service;


import ru.anime.okami.model.Anime;

import java.util.List;

public interface AnimeService {
    Anime createAnime(Anime anime);

    List<Anime> getAllAnime();

    Anime getAnimeById(long id);

    Anime updateAnime(Anime anime, long id);

    void deleteAnimeById(long id);

    List<Anime> getAnimeByCategory(long categoryId);
}
