package ru.filippov.neat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.filippov.neat.entity.ExperimentResult;

import java.util.Optional;

@Repository
public interface ExperimentResultsRepository extends JpaRepository<ExperimentResult, Long> {

    Optional<ExperimentResult> findById(Long id);


}
