package com.rdbac.rdbac.newSystem.ClientCredentials.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ClientFilter extends OncePerRequestFilter{

    private final ClientTokenService clientTokenService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        log.info(request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && !authHeader.isEmpty()) {
            String aceessToken = authHeader.substring(7);
            log.info("aceessToken is  {} " , aceessToken);
            //if valid acccess token. 
            if(clientTokenService.validate(aceessToken)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(aceessToken, null, null);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        filterChain.doFilter(request, response);
    }

}
