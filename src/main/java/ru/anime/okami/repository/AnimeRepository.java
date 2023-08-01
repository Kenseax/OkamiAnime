package ru.anime.okami.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anime.okami.model.Anime;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
}
