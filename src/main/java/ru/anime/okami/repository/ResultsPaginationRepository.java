package ru.anime.okami.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.anime.okami.generatedPojo.Results;

import java.util.Optional;

public interface ResultsPaginationRepository extends PagingAndSortingRepository<Results, Long> {

    Optional<Page<Results>> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);
}
