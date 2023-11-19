package ru.anime.okami.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anime.okami.generatedPojo.MaterialData;

@Repository
public interface MaterialDataRepository extends JpaRepository<MaterialData, Long> {
}
