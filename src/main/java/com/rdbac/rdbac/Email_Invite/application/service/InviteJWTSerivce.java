package com.rdbac.rdbac.Email_Invite.application.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.rdbac.rdbac.Email_Invite.application.dtop.Email_Invite_DTO;
import com.rdbac.rdbac.Email_Invite.domain.model.Email_Invitation;
import com.rdbac.rdbac.Email_Invite.domain.model.Invite_Stauts;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InviteJWTSerivce {

     private SecretKey secretKey;
     public InviteJWTSerivce() {
        keygenrate();
        //System.out.println(secretKey);

     }
// REVIEW (Post-MVP / Production Hardening):
// ------------------------------------------------------------
// ⚠️ Current Behavior:
// This method generates a new HMAC-SHA256 secret key on every application startup.
// That means all previously issued tokens or signed data will become invalid 
// whenever the service restarts — this is acceptable only for local development or MVP demos.
//
// ✅ Production Expectation:
// 1️⃣ Move key management to a secure, persistent source — for example:
//     - Load the key from environment variables or external configuration (Spring Config Server, AWS Secrets Manager, Vault).
//     - Never hardcode or auto-generate secrets at runtime in production environments.
// 2️⃣ Implement a key rotation strategy if long-term validity is required.
// 3️⃣ Log minimal information about key initialization — avoid logging raw key values.
//
// 💡 Suggestion:
// For now, mark this as MVP-safe, but plan a follow-up ticket for productionizing key management 
// before deployment to any shared or persistent environment.
     private void keygenrate() {
        try {
            KeyGenerator keyGenerator =  KeyGenerator.getInstance("HmacSHA256");
            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

     }

// REVIEW (Post-MVP / Token Policy):
// ------------------------------------------------------------
// ⚠️ Token validity is currently hardcoded (10 minutes). 
// For production, externalize this duration into configuration 
// (e.g., application.yml -> jwt.expiration=3600000) to allow dynamic control.
//
// ✅ Recommended: 1 hour (1000 * 60 * 60) for standard invites, 
// or shorter if security sensitivity is high.
//
// 💡 Always ensure expiration aligns with key rotation and refresh policies.

     public  String  genrateJwtToken(Email_Invite_DTO email_Invite_DTO,String user_sent,String invitation_id) {
        Map<String,Object> map  =  new  HashMap<>();
        map.put("email", email_Invite_DTO);
        map.put("sent", user_sent);
        map.put("status", Invite_Stauts.PENDING);

        
        return  Jwts.builder()

                    .subject(invitation_id)
                    .id(UUID.randomUUID().toString())
                    .issuedAt(new  Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis()+(1000*60*60)))
                    .claims(map)
                    .signWith(secretKey).compact();
                    
    }
    public String  get_invitation_id(String token) {
        final  Claims  claims  =  Jwts
                                        .parser()
                                        .verifyWith(secretKey)
                                        .build()
                                        .parseSignedClaims(token)
                                        .getPayload();
        log.info(claims.getSubject()+"   "+ claims.getExpiration());
        log.debug(claims.toString());;
        return claims.getSubject();
    }

    public boolean validate(Email_Invitation email_Invitation, String token) {
        // TODO Auto-generated method stub
        String  token_userId  = get_invitation_id(token);
        return  ((email_Invitation.getInvitationId().equals(token_userId)) && email_Invitation.getStauts().equals(Invite_Stauts.PENDING) &&   !isTokenExpired(token));  
    }
    private  boolean  isTokenExpired(String  token) {
        final  Claims  claims  =  Jwts
                                    .parser()
                                    .verifyWith(secretKey)
                                    .build()
                                    .parseSignedClaims(token)
                                    .getPayload();
        log.info(claims.getSubject()+"   "+ claims.getExpiration());
        return  claims.getExpiration().before(new  Date());

    }
}
