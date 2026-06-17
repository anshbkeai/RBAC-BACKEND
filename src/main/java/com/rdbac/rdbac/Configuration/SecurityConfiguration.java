package com.rdbac.rdbac.Configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbac.rdbac.Filter.CorrelationIdFilter;
import com.rdbac.rdbac.Filter.JwtFilter;
import com.rdbac.rdbac.ServiceImplementation.JWTServiceImplementation;
import com.rdbac.rdbac.ServiceImplementation.UserServiceImplementation;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CorrelationIdFilter correlationIdFilter;
    private final JWTServiceImplementation jwtServiceImplementation;
    private final UserServiceImplementation userServiceImplementation;
    private final ObjectMapper objectMapper;

    
    @Value("${REACT_APP_URL}")
    private String reactAppUri;
   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity ) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtServiceImplementation, userServiceImplementation , objectMapper);
            httpSecurity.csrf(csrf -> csrf.disable())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .cors(cors -> cors.configurationSource(corsConfiguration()))
                    .authorizeHttpRequests(auth -> {
                        auth.requestMatchers("/auth/**", "/email/accept/**","/access/check" , "/review/**").permitAll();

                        auth.anyRequest().authenticated();
                    })
                    .addFilterBefore(correlationIdFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    ;

        return  httpSecurity.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfiguration() {
        CorsConfiguration  corsConfiguration   =  new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(reactAppUri));   //  in th  Prod  we  need  to  se  the   React app  hosted  Url  
        corsConfiguration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource  urlBasedCorsConfigurationSource   =  new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return  urlBasedCorsConfigurationSource;
    } 
}
