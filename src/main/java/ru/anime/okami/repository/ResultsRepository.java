package ru.anime.okami.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anime.okami.generatedPojo.Results;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultsRepository extends JpaRepository<Results, Long> {

    Optional<Results> findByTitle(String title);
    Optional<Results> findFirstByTitle(String title);

    Optional<Results> findById(String id);


}
