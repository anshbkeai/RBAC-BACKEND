package com.rdbac.rdbac.newSystem.ClientCredentials.api;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientAccessRequest;
import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientAccessResponse;
import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientCredentialRequest;
import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientCredentialResponse;
import com.rdbac.rdbac.newSystem.ClientCredentials.service.ClientFacade;
import com.rdbac.rdbac.newSystem.ClientCredentials.service.ClientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RequestMapping("/oauth")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ClientAppController {

    private final ClientService clientService;
    private final ClientFacade clientFacade;
    @PostMapping("/token")
    public ResponseEntity<ClientCredentialResponse> genrateToken(@RequestBody ClientCredentialRequest clientCredentialRequest) throws NoSuchAlgorithmException {
        // validated and then shipping it to new 
        // client Servei -> ClienrId-> db fecht mathc hash -> and then will issue the token . i need this . auth follou the auth 

        ClientCredentialResponse clientCredentialResponse = clientService.genrateToken(clientCredentialRequest);
        return ResponseEntity.ok(clientCredentialResponse);
    }

    @GetMapping("/get")
    public String getMethodName() {
        return "OK";
    }

    @PostMapping("/access/check")
    public ResponseEntity<ClientAccessResponse> accessCheck(@RequestBody ClientAccessRequest clientAccessRequest) { 

        return ResponseEntity.ok(clientFacade.verify(clientAccessRequest));
    }
    
    
}
