package ru.filippov.neat.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.filippov.neat.entity.Experiment;
import ru.filippov.neat.entity.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
    @Query("SELECT nc FROM Experiment nc WHERE nc.project.id = ?1 ORDER BY nc.creationDate DESC")
    List<Experiment> findAllByProjectId(Long userId);

    @EntityGraph(value = "Experiment.project")
    Optional<Experiment> findById(Long id);



}
