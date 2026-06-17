package com.rdbac.rdbac.newSystem.ClientCredentials.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientAuthResponse;
import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientOrganisationRequest;
import com.rdbac.rdbac.newSystem.ClientCredentials.service.ClientService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientAuthResponse> createClientCredentials(@RequestBody ClientOrganisationRequest organisationId) {
        String authencticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        ClientAuthResponse clientAuthResponse = clientService.createCrenditals(organisationId.organisationId(), authencticatedUser);
        return new ResponseEntity<ClientAuthResponse>(clientAuthResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Boolean> isAlreadyCredentialsExist(@RequestBody ClientOrganisationRequest organisationId) {
        String authencticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(clientService.isAlreadyCredentialsExist(organisationId.organisationId(), authencticatedUser)) {
            return new ResponseEntity<>( true, HttpStatus.CONFLICT);
        }   
        else {
            return ResponseEntity.ok(false);
        }
    }
    
    @DeleteMapping
    public ResponseEntity<Boolean> deleteClientCredentails(@RequestBody ClientOrganisationRequest organisationId) {
        String authencticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<Boolean>(clientService.deleteClientCredentails(organisationId.organisationId(), authencticatedUser), HttpStatus.NO_CONTENT);
    }
     
}
