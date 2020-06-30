package ru.filippov.neat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.filippov.neat.entity.NeatConfig;

import java.util.List;

@Repository
public interface NeatConfigRepository extends JpaRepository<NeatConfig, Long> {
    @Query("SELECT nc FROM NeatConfig nc WHERE nc.project.id = ?1 ORDER BY nc.creationDate DESC")
    List<NeatConfig> findAllByProjectId(Long userId);
}
