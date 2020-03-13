package ru.filippov.neat.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.filippov.neat.domain.Auth;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.exceptions.RefreshTokenNotExists;
import ru.filippov.neat.repository.AuthRepository;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class AuthServiceImpl {

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

    public Auth findRefreshToken(String refreshToken) throws RefreshTokenNotExists{
        Optional<Auth> refreshTokenFromBase = authRepository.findByRefreshToken(refreshToken);
        return refreshTokenFromBase.orElseThrow(RefreshTokenNotExists::new);
    }

    public void updateAuth(Auth auth) {
            authRepository.save(auth);
    };

    public void deleteToken(Auth refreshTokenFromBase) {
        authRepository.delete(refreshTokenFromBase);
    }


    public Auth findPreviousRefreshToken(String refreshToken) {
        return authRepository.findByPreviousToken(refreshToken).get();
    }
}
