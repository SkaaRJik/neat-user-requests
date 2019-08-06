package ru.filippov.neatvue.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.filippov.neatvue.domain.User;
import ru.filippov.neatvue.service.user.UserPrinciple;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${app.jwt.Secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.Expiration}")
    private int JWT_EXPIRATION;

    @Value("${app.jwt.Prefix}")
    private String JWT_PREFIX;

    @Value("${app.jwt.Header}")
    private String JWT_HEADER;

    public String generateJwtToken(Authentication authentication) {

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        String token = JWT_PREFIX + JWT.create()
                .withSubject(userPrincipal.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .sign(HMAC512(JWT_SECRET.getBytes()));

        logger.debug(token);

        return token;



        /*return Jwts.builder()
		                .setSubject((userPrincipal.getUsername()))
		                .setIssuedAt(new Date())
		                .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION *1000))
		                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
		                .compact();*/
    }
    
    public boolean validateJwtToken(String authToken) {
       /* try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }*/
        
        return false;
    }
    
    public String getUserNameFromJwtToken(String token) throws JWTVerificationException {
        /*return Jwts.parser()
			                .setSigningKey(JWT_SECRET)
			                .parseClaimsJws(token)
			                .getBody().getSubject();*/

        return JWT.require(HMAC512(JWT_SECRET.getBytes()))
                .build()
                .verify(token.substring(JWT_PREFIX.length()))
                .getSubject();
    }

    public String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(JWT_HEADER);

        if (authHeader != null && authHeader.startsWith(JWT_PREFIX)) {
            return authHeader.substring(JWT_PREFIX.length());
        }

        return null;
    }
}