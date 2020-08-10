package ru.filippov.neat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.filippov.neat.entity.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findById(Long id);
    @Query("SELECT pr FROM Project pr WHERE pr.user.id = ?1")

    Page<Project> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT pr FROM Project pr WHERE pr.name = ?1 AND pr.user.id = ?2")
    Optional<Project> findOneByNameAndUserId(String name, Long userId);

}
