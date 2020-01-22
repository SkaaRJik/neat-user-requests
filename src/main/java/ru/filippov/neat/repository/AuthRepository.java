package ru.filippov.neat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.filippov.neat.domain.Auth;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findById(Long id);
    Optional<Auth> findByRefreshToken(String token);
    Optional<Auth> findByIp(String ip);


}
