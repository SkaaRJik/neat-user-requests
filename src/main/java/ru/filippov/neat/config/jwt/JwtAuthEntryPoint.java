package ru.filippov.neat.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) 
                        		 throws IOException, ServletException {

        log.error(e.getMessage());
        if(response.getStatus() == JwtStatus.EXPIRED_TOKEN.value){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired");
        } else if(response.getStatus() == JwtStatus.CHANGED_TOKEN.value){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token was changed");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }


    }
}