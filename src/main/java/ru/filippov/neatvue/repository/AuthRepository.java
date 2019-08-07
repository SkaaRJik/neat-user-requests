package ru.filippov.neatvue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.filippov.neatvue.domain.Auth;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findById(Long id);
    Optional<Auth> findByRefreshToken(String token);
    Optional<Auth> findByIp(String ip);


}
