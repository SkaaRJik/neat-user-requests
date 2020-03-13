package ru.filippov.neat.config.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface TokenProvider {
    String generateAccessToken(String username, String[] roles);

    String generateAccessToken(String username, String[] roles, long expirationTime);

    String generateRefreshToken(String username);

    String generateRefreshToken(String username, long expirationTime);


    boolean validateJwtToken(String authToken);

    String getUserNameFromJwtToken(String token) throws JWTVerificationException;

    String getToken(HttpServletRequest request);

    long getAccessTokenExpiration();
    long getRefreshTokenExpiration();

    List<GrantedAuthority> getAuthoritiesFromJwtToken(String actionToken);
}
