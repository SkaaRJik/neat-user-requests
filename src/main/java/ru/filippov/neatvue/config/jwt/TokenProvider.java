package ru.filippov.neatvue.config.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface TokenProvider {
    String generateJwtToken(Authentication authentication);

    boolean validateJwtToken(String authToken);

    String getUserNameFromJwtToken(String token) throws JWTVerificationException;

    String getJwt(HttpServletRequest request);
}
