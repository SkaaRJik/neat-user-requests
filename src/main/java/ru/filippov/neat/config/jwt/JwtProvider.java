package ru.filippov.neat.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.filippov.neat.domain.Role;
import ru.filippov.neat.service.user.UserPrinciple;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
@Slf4j
public class JwtProvider implements TokenProvider {



    @Value("${app.jwt.secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.access.expiration}")
    private long JWT_ACCESS_EXPIRATION;

    @Value("${app.jwt.access.prefix}")
    private String JWT_PREFIX;

    @Value("${app.jwt.access.header}")
    private String JWT_HEADER;

    @Value("${app.jwt.refresh.expiration}")
    private long JWT_REFRESH_EXPIRATION;

    @Override
    public String generateAccessToken(String username, String[] roles) {
        return generateAccessToken(username, roles,this.JWT_ACCESS_EXPIRATION);
    }

    @Override
    public String generateAccessToken(String username, String[] roles, long expirationTime) {


        String token = JWT_PREFIX + JWT.create()
                .withSubject(username)
                .withArrayClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(HMAC512(JWT_SECRET.getBytes()));

        return token;
    }

    @Override
    public String generateRefreshToken(String username) {
        return generateRefreshToken(username, this.JWT_REFRESH_EXPIRATION);

    }

    @Override
    public String generateRefreshToken(String username, long expirationTime) {



        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(HMAC512(JWT_SECRET.getBytes()));

        return token;
    }



    @Override
    public boolean validateJwtToken(String token) throws JWTVerificationException {

        JWT.require(HMAC512(JWT_SECRET.getBytes()))
                .build()
                .verify(token);

        return true;
    }

    @Override
    public String getUserNameFromJwtToken(String token) throws JWTVerificationException {
        return JWT.require(HMAC512(JWT_SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

    @Override
    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(JWT_HEADER);

        if (authHeader != null && authHeader.startsWith(JWT_PREFIX)) {
            return authHeader.substring(JWT_PREFIX.length());
        }

        return null;
    }

    @Override
    public long getAccessTokenExpiration() {
        return this.JWT_ACCESS_EXPIRATION;
    }

    @Override
    public long getRefreshTokenExpiration() {
        return this.JWT_REFRESH_EXPIRATION;
    }

    @Override
    public List<GrantedAuthority> getAuthoritiesFromJwtToken(String actionToken) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Arrays.stream(JWT.require(HMAC512(JWT_SECRET.getBytes()))
                .build()
                .verify(actionToken)
                .getClaim("roles").asArray(String.class)).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        return authorities;
    }
}