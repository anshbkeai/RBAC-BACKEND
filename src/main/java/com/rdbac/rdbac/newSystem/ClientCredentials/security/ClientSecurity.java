package com.rdbac.rdbac.newSystem.ClientCredentials.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ClientSecurity {

    private final UrlBasedCorsConfigurationSource configurationSource;
    private final ClientTokenService clientTokenService;
    

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainClient(HttpSecurity httpSecurity) throws Exception {
        
        ClientFilter clientFilter = new ClientFilter(clientTokenService);
        httpSecurity.securityMatcher("/oauth/**")
                    .csrf(csrf -> csrf.disable())
                    .cors( cors -> cors.configurationSource(configurationSource))
                    .authorizeHttpRequests(auth -> {
                        auth.requestMatchers("/oauth/token").permitAll();
                        auth.anyRequest().authenticated();
                    })
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(clientFilter, UsernamePasswordAuthenticationFilter.class)
                    ;

                    // add the filters 
        return httpSecurity.build();
    }
}
