package ru.filippov.neat.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.filippov.neat.service.user.UserPrinciple;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
@Slf4j
public class JwtProvider implements TokenProvider {



    @Value("${app.jwt.Secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.Access_Expiration}")
    private int JWT_ACCESS_EXPIRATION;

    @Value("${app.jwt.Refresh_Expiration}")
    private int JWT_REFRESH_EXPIRATION;

    @Value("${app.jwt.Prefix}")
    private String JWT_PREFIX;

    @Value("${app.jwt.Header}")
    private String JWT_HEADER;

    @Override
    public String generateAccessToken(Authentication authentication) {
        return generateAccessToken(authentication, this.JWT_ACCESS_EXPIRATION);
    }

    @Override
    public String generateAccessToken(Authentication authentication, int expirationTime) {

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        String token = JWT_PREFIX + JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim("role", authentication
                        .getAuthorities()
                        .stream().
                                map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new)
                )
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(HMAC512(JWT_SECRET.getBytes()));

        return token;
    }

    @Override
    public String generateRefreshToken(Authentication authentication) {
        return generateRefreshToken(authentication, this.JWT_ACCESS_EXPIRATION);

    }

    @Override
    public String generateRefreshToken(Authentication authentication, int expirationTime) {

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        String token = JWT_PREFIX + JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(HMAC512(JWT_SECRET.getBytes()));

        return token;
    }



    @Override
    public boolean validateJwtToken(String token) {


        try{
            JWT.require(HMAC512(JWT_SECRET.getBytes()))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        
        return false;
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
    public int getAccessTokenExpiration() {
        return this.JWT_ACCESS_EXPIRATION;
    }

    @Override
    public int getRefreshTokenExpiration() {
        return this.JWT_REFRESH_EXPIRATION;
    }
}