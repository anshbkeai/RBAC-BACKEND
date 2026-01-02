package com.rdbac.rdbac.Email_Invite.interfaces.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.Email_Invite.application.dtop.Email_Invite_DTO;
import com.rdbac.rdbac.Email_Invite.application.service.Email_Invite_Service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



/**
 * REST controller for handling email invitation related endpoints.
 * <p>
 * Exposes endpoints to send invitation emails and to verify invitation tokens.
 * </p>
 *
 * <ul>
 *   <li><b>POST /email/invite</b>: Sends an invitation email to a user.</li>
 *   <li><b>GET /email/accept/{token}</b>: Verifies the invitation token sent to the user's email.</li>
 * </ul>
 *
 * <p>
 * Note: This controller contains open routes that may require updates in security filters.
 * </p>
 *
 * @author [Your Name]
 */
@RestController
@RequestMapping("/email")
public class Invite_Email_RestController {

    private final Email_Invite_Service email_Invite_Service;
    public Invite_Email_RestController(Email_Invite_Service email_Invite_Service) {
        this.email_Invite_Service = email_Invite_Service;
    }

    private String getAppUser_Email() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            // we. can. log
            return authentication.getName();
        }
        else {
            throw new RuntimeErrorException(new Error());
        }
    }
    @PostMapping("/invite")
    public ResponseEntity<Map< String, String>> inviteUser(@RequestBody Email_Invite_DTO email_Invite_DTO) {
    
        return ResponseEntity.ok(Map.of("message", email_Invite_Service.sendEmailInvite(email_Invite_DTO, getAppUser_Email())));
        
        
    }


   // NOTE: This endpoint is intentionally left unauthenticated (open route).
// If you modify this path, make sure to update the security configuration (e.g., WebSecurityConfigurer)
// to keep this endpoint accessible without authentication. Review all security filters and
    @GetMapping("/accept/{token}")
    public ResponseEntity<Map< String, String>> Verify_Token(@PathVariable String token) {
        //. eamilSerive .valid ("User Successfully Added ", HttpStatus.OK)Token is Invalid

        if(Boolean.TRUE.equals(email_Invite_Service.isValid(token))) return new ResponseEntity<Map<String,String>>(Map.of("message", "User Successfully Added"), HttpStatus.OK);
        else return new ResponseEntity<Map<String,String>>(Map.of("message", "Token is Invalid"), HttpStatus.CONFLICT);
    }
    

    @GetMapping("/bulk-email")
    public ResponseEntity<String> sendBulk() {
        for (int i = 0; i < 50; i++) {
            Email_Invite_DTO dto = new Email_Invite_DTO();
            dto.setOrganisationId("e87f352b-710d-49cb-8898-6c3937b25b7a");
            dto.setInvitedUserEmail("dasas62488@dextrago.com");
            email_Invite_Service.sendEmailInvite(dto, getAppUser_Email()); // this is async
        }
        return ResponseEntity.ok("Queued 50 emails.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> getEmailByKeyWord(@RequestParam String query) {
        return ResponseEntity.ok(email_Invite_Service.findByEmailStartingWith(query));
    }
    
    
    

}
