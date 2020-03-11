package ru.filippov.neat.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.filippov.neat.domain.Auth;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.repository.AuthRepository;

import java.util.Date;

@Slf4j
@Service
public class AuthService {

    @Autowired
    AuthRepository authRepository;

    @Transactional
    public void addToken(User user, String token, Date expirationDate){
        Auth auth = Auth.builder()
                .user(user)
                .refreshToken(token)
                .expirationDate(expirationDate)
                .build();
        authRepository.save(auth);
    }



}
