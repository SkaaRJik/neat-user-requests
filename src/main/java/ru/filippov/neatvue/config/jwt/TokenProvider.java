package ru.filippov.neatvue.config.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.Data;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;


public interface TokenProvider {
    String generateAccessToken(Authentication authentication);

    String generateAccessToken(Authentication authentication, int expirationTime);

    String generateRefreshToken(Authentication authentication);

    String generateRefreshToken(Authentication authentication, int expirationTime);


    boolean validateJwtToken(String authToken);

    String getUserNameFromJwtToken(String token) throws JWTVerificationException;

    String getToken(HttpServletRequest request);

    int getAccessTokenExpiration();
    int getRefreshTokenExpiration();
}
