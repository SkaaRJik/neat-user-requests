package ru.filippov.neat.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.filippov.neat.config.jwt.TokenProvider;
import ru.filippov.neat.entity.Auth;
import ru.filippov.neat.entity.User;
import ru.filippov.neat.dto.UserAuthDetailsResponse;
import ru.filippov.neat.dto.SignInDto;
import ru.filippov.neat.dto.SignUpDto;
import ru.filippov.neat.dto.TokenDto;
import ru.filippov.neat.exception.AvatarUploadException;
import ru.filippov.neat.exception.RefreshTokenNotExists;
import ru.filippov.neat.exception.UserNotFoundException;
import ru.filippov.neat.service.auth.AuthServiceImpl;
import ru.filippov.neat.service.user.UserDetailsServiceImpl;
import ru.filippov.neat.service.user.UserPrincipal;

import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

@RestController
@Slf4j
public class AuthRestAPI {

    final private AuthenticationManager authenticationManager;

    final private UserDetailsServiceImpl userService;

    final private AuthServiceImpl authService;

    final private TokenProvider jwtProvider;

    public AuthRestAPI(AuthenticationManager authenticationManager, UserDetailsServiceImpl userService, AuthServiceImpl authService, TokenProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/avatar")
    public ResponseEntity uploadAvatar(@AuthenticationPrincipal UserPrincipal user, @RequestParam("file") MultipartFile file){
        try {
            userService.saveAvatar(user.getId(), file);
            return ResponseEntity.ok("UPLOADED");
        } catch (UserNotFoundException e) {
            log.error("AuthRestAPI.uploadAvatar", e);
            return new ResponseEntity("USER_NOT_EXISTS", HttpStatus.NOT_FOUND);
        } catch (AvatarUploadException e) {
            log.error("AuthRestAPI.uploadAvatar", e);
            return new ResponseEntity("CANT_UPLOAD_THE_FILE", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/avatar/{username}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity downloadFile(@PathVariable String username) {

        UrlResource resource = null;
        try {
            resource = userService.getAvatar(username);
            final String[] split = resource.getFilename().split(".");

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);



        } catch (UserNotFoundException e) {
            log.error("AuthRestAPI.downloadFile", e);
            return new ResponseEntity( HttpStatus.OK);
        } catch (MalformedURLException e) {
            log.error("AuthRestAPI.downloadFile", e);
            return new ResponseEntity( HttpStatus.OK);
        }




    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInDto loginRequest) {

        boolean userExists = false;

        if(loginRequest.getUsername().indexOf('@')!= -1) {
            userExists = userService.existsByEmail(loginRequest.getUsername());
        } else {
            userExists = userService.existsByUsername(loginRequest.getUsername());
        }

        if(!userExists){
            return new ResponseEntity<String>("Пользователя с таким Логином / Email не существует",
                    HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        User user = ((UserPrincipal) authentication.getPrincipal()).toUser();


        String[] roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);

        String refreshToken = jwtProvider.generateRefreshToken(user.getUsername());



        authService.addToken(user,
                refreshToken, new Date(new Date().getTime() + jwtProvider.getRefreshTokenExpiration()));

        String accessToken = jwtProvider.generateAccessToken(user.getUsername(), roles);

        UserAuthDetailsResponse profile = UserAuthDetailsResponse
                .build(TokenDto.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .accessTokenExpiredAfterMilliseconds(jwtProvider.getAccessTokenExpiration())
                                .refreshTokenExpiredAfterMilliseconds(jwtProvider.getRefreshTokenExpiration())
                                .build(),
                        (UserPrincipal) authentication.getPrincipal());



        return ResponseEntity.ok(profile);
    }

    @GetMapping("/email-exist")
    public ResponseEntity<Boolean> isEmailExist(@RequestParam String email){
        Boolean existsByEmail = userService.existsByEmail(email);

        return new ResponseEntity<Boolean>(existsByEmail,
                HttpStatus.OK);

    }

    @GetMapping("/username-exist")
    public ResponseEntity<Boolean> isUsernameExist(@RequestParam String username){
        Boolean existsByUsername = userService.existsByUsername(username);

        return new ResponseEntity<Boolean>(existsByUsername,
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
            log.error("AuthRestAPI.registerUser", e);
        }

        return ResponseEntity.ok().body("Ползователь зарегистрирован успешно. На ваш e-mail отправлено письмо с подтверждением регистрации.");
    }

    @GetMapping("/refresh-tokens")
    public ResponseEntity<Object> refreshTokens(@Valid @RequestParam String refreshToken) {
        try {




            boolean shouldDeleteTokenFromBase = false;
            try{
                jwtProvider.getUserNameFromJwtToken(refreshToken);
            } catch (TokenExpiredException e) {
                shouldDeleteTokenFromBase = true;
            }


            Auth previousRefreshToken = authService.findPreviousRefreshToken(refreshToken);
            if(previousRefreshToken != null){
                authService.deleteToken(previousRefreshToken);
                log.error("Old refresh token was used by user", previousRefreshToken.getUser().getUsername());
                return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Кто-то воспользовался вашими старыми данными аутентификации");
            }


            Auth refreshTokenFromBase = authService.findRefreshToken(refreshToken);

            if( shouldDeleteTokenFromBase ) {
                log.error("Refresh token expired for user: ", refreshTokenFromBase.getUser().getUsername());
                authService.deleteToken(refreshTokenFromBase);
                return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Время жизни токена истекло");
            }

            User user = refreshTokenFromBase.getUser();
            String newRefreshToken = jwtProvider.generateRefreshToken(user.getUsername());

            refreshTokenFromBase.setPreviousToken(refreshToken);
            refreshTokenFromBase.setRefreshToken(newRefreshToken);
            refreshTokenFromBase.setExpirationDate(new Date(new Date().getTime()+ jwtProvider.getRefreshTokenExpiration()));

            authService.updateAuth(refreshTokenFromBase);

            String[] roles = user.getRoles().stream().map(role -> role.name()).toArray(String[]::new);
            String accessToken = jwtProvider.generateAccessToken(user.getUsername(), roles);

            TokenDto tokens = new TokenDto(
                    accessToken, newRefreshToken, jwtProvider.getAccessTokenExpiration(), jwtProvider.getRefreshTokenExpiration()
            );
            return ResponseEntity.ok(tokens);
        } catch (RefreshTokenNotExists e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body("Токена не существует в базе");
        } catch (Exception e) {
            log.error("AuthRestAPI.refreshTokens", e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Невозможно обновить токен");
        }


    }


}
