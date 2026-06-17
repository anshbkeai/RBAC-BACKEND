package com.rdbac.rdbac.newSystem.ClientCredentials.security;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class ClientTokenService {

    private SecretKey secretKey;


    public ClientTokenService(@Value("${CLIENT_SECRET_BASE64}") String clientTokenSecret) {
       try {
         byte[] bs= Base64.getDecoder().decode(clientTokenSecret);
         this.secretKey = new SecretKeySpec(bs, "HmacSHA256");
         System.out.println(secretKey);
       }
       catch(Exception e) {
        System.out.println(e.getMessage());
       }
    }

    public String genrateClientToken(String clientId) {
        return Jwts.builder()
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + Duration.ofHours(2).toMillis()))
                    .subject(clientId)
                    .signWith(secretKey)
                    .compact();
    }

    public String  getClientId(String token) {
        final  Claims  claims  =  Jwts
                                        .parser()
                                        .verifyWith(secretKey)
                                        .build()
                                        .parseSignedClaims(token)
                                        .getPayload();
        return claims.getSubject();
    }

    public boolean validate(String token) {
        return    !isTokenExpired(token);  
    }
    private  boolean  isTokenExpired(String  token) {
        final  Claims  claims  =  Jwts
                                    .parser()
                                    .verifyWith(secretKey)
                                    .build()
                                    .parseSignedClaims(token)
                                    .getPayload();
        return  claims.getExpiration().before(new  Date());

    }

    
}
