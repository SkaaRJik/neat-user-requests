package ru.filippov.neat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.filippov.neat.domain.Auth;
import ru.filippov.neat.domain.Project;
import ru.filippov.neat.domain.User;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findById(Long id);
    Optional<Project> findByUser(User user);
}
