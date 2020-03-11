package ru.filippov.neat.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.filippov.neat.config.jwt.TokenProvider;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.dto.ProfileDto;
import ru.filippov.neat.dto.SignInDto;
import ru.filippov.neat.dto.SignUpDto;
import ru.filippov.neat.dto.TokenDto;
import ru.filippov.neat.service.auth.AuthService;
import ru.filippov.neat.service.user.UserDetailsServiceImpl;
import ru.filippov.neat.service.user.UserPrinciple;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@RestController
@Slf4j
@CrossOrigin
public class AuthRestAPI {

    final private AuthenticationManager authenticationManager;

    final private UserDetailsServiceImpl userService;

    final private AuthService authService;

    final private TokenProvider jwtProvider;

    public AuthRestAPI(AuthenticationManager authenticationManager, UserDetailsServiceImpl userService, AuthService authService, TokenProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInDto loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);




        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        User user = ((UserPrinciple) userService.loadUserByUsername(loginRequest.getEmail())).toUser();

        authService.addToken(user,
                refreshToken, new Date(new Date().getTime() + jwtProvider.getRefreshTokenExpiration()));

        String accessToken = jwtProvider.generateAccessToken(authentication);

        ProfileDto profile = ProfileDto.build(new TokenDto(
                accessToken, refreshToken, jwtProvider.getAccessTokenExpiration(), jwtProvider.getRefreshTokenExpiration()
        ),authentication);



        return ResponseEntity.ok(profile);
    }

    @GetMapping("/email-exist")
    public ResponseEntity<Boolean> isEmailExist(@RequestParam String email){
        Boolean existsByEmail = userService.existsByEmail(email);

        return new ResponseEntity<Boolean>(existsByEmail,
                HttpStatus.OK);

    }


    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpDto signUpRequest) {
        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Ошибка. Такой e-mail уже зарегистрирован в системе",
                    HttpStatus.BAD_REQUEST);
        }

        try {
            userService.registrate(signUpRequest);
        } catch (PSQLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body("Ползователь зарегистрирован успешно. На ваш e-mail отправлено письмо с подтверждением регистрации.");
    }
}