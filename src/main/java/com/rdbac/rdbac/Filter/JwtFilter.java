package com.rdbac.rdbac.Filter;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbac.rdbac.Pojos.App_User;
import com.rdbac.rdbac.ServiceImplementation.JWTServiceImplementation;
import com.rdbac.rdbac.ServiceImplementation.UserServiceImplementation;
import com.rdbac.rdbac.exceptions.ApiError;
import com.rdbac.rdbac.exceptions.InValidJWT;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter  {

    private JWTServiceImplementation jwtServiceImplementation;
    private UserServiceImplementation userServiceImplementation;
    private final ObjectMapper objectMapper;
    public JwtFilter(JWTServiceImplementation jwtServiceImplementation,
                    UserServiceImplementation userServiceImplementation,
                    ObjectMapper objectMapper
    ) {
        this.userServiceImplementation = userServiceImplementation;
        this.jwtServiceImplementation = jwtServiceImplementation;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        // get the Token from the request header and verify that
        String requestUri = request.getRequestURI();
        log.info(requestUri);
        if(requestUri.startsWith("/auth") || requestUri.contains("/email/accept") || requestUri.contains("/access/check") ||  requestUri.contains("/review")) {filterChain.doFilter(request, response); return ;}
        String autheader = request.getHeader("Authorization");
        try {
            if(!autheader.isEmpty() && autheader.startsWith("Bearer")) {
                String token = autheader.substring(7); // you have the. token . i donot have the password maccah 
                String user_email = jwtServiceImplementation.getUserEmail(token);
    
                if(!user_email.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // you nned to fetch about the user here adn get the data. // now so we can also add about the 
                    App_User userDetails = (App_User) userServiceImplementation.loadUserByUsername(user_email);
                    if(jwtServiceImplementation.validate(userDetails, token)) {
                        UsernamePasswordAuthenticationToken  token2 =  new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                         token2.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(token2);
            
                            log.info("Authentication successful for user: " + user_email);
                    }
                    else {
                        throw new InValidJWT();
                    }
                }
    
                // 
            }     
        } 
        catch (ExpiredJwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(
                objectMapper.writeValueAsString(
                    new ApiError("JWT expired", "401", request.getRequestURI() , LocalDateTime.now() )
                )
            );
            return;
        }
        catch (JwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }

}
