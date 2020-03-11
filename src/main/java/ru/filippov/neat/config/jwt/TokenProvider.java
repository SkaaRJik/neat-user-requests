package ru.filippov.neat.config.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;


public interface TokenProvider {
    String generateAccessToken(Authentication authentication);

    String generateAccessToken(Authentication authentication, long expirationTime);

    String generateRefreshToken(Authentication authentication);

    String generateRefreshToken(Authentication authentication, long expirationTime);


    boolean validateJwtToken(String authToken);

    String getUserNameFromJwtToken(String token) throws JWTVerificationException;

    String getToken(HttpServletRequest request);

    long getAccessTokenExpiration();
    long getRefreshTokenExpiration();
}
