package ru.anime.okami.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anime.okami.model.Episode;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
}
