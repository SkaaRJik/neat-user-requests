package ru.filippov.neatvue.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.filippov.neatvue.domain.Auth;
import ru.filippov.neatvue.domain.User;
import ru.filippov.neatvue.repository.AuthRepository;

@Slf4j
@Service
public class AuthService {

    @Autowired
    AuthRepository authRepository;

    @Transactional
    public void addToken(User user, String token, String clientIp, String browser, String os){
        Auth auth = Auth.builder()
                .user(user)
                .refreshToken(token)
                .ip(clientIp)
                .browser(browser)
                .os(os)
                .build();
        authRepository.save(auth);
    }



}
