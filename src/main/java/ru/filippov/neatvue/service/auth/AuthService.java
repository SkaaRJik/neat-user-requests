package ru.filippov.neatvue.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.filippov.neatvue.domain.Auth;
import ru.filippov.neatvue.repository.AuthRepository;

import java.util.Optional;

@Slf4j
public class AuthService {

    @Autowired
    AuthRepository authRepository;

    @Transactional
    public Optional<Auth> loadAuthById(Long id){

    }

    @Transactional
    public Optional<Auth> loadAuthByIp(String ip){

    }

    @Transactional
    public Optional<Auth> loadAuthByRefreshToken(String token)



}
