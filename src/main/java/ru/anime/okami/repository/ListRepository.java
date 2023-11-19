package ru.anime.okami.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anime.okami.generatedPojo.List;

@Repository
public interface ListRepository extends JpaRepository<List, Long> {
}
