package ru.filippov.neat.exception;


import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /*@ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity vehicleNotFound(UserNotFoundException ex, WebRequest request) {
        log.debug("handling UserNotFoundException...");
        return null;
    }*/

    @ExceptionHandler(value = {TokenExpiredException.class})
    public ResponseEntity jwtIsExpired(TokenExpiredException ex, WebRequest request) {
        log.info("EXCEPTION HANDLER WORKS!");
        return null;
    }

}
