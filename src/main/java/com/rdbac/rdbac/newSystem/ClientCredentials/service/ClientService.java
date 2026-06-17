package com.rdbac.rdbac.newSystem.ClientCredentials.service;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.rdbac.rdbac.ApiKey.config.HashGenrator;
import com.rdbac.rdbac.Organisation.Service.OrganisationService;
import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientAuthResponse;
import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientCredentialRequest;
import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientCredentialResponse;
import com.rdbac.rdbac.newSystem.ClientCredentials.model.ClientApp;
import com.rdbac.rdbac.newSystem.ClientCredentials.repository.ClientRepository;
import com.rdbac.rdbac.newSystem.ClientCredentials.security.ClientTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final OrganisationService organisationService;
    private final HashGenrator hashGenrator;
    private final ClientTokenService clientTokenService;

    public ClientAuthResponse createCrenditals(String organisationId, String authenticatedUser) {
        try {
            boolean isallowed = organisationService.isAdminUserByEmail(organisationId, authenticatedUser);
            if(isallowed) {
                if(clientRepository.findByOrganizationID(organisationId).isPresent()) {
                    throw new RuntimeException("Already Created Credentilas");
                }

                String organisationIdHash = hashGenrator.Sha256Hashing(organisationId);
                Instant instant = Instant.now();
                StringBuilder clientSecrect  = new StringBuilder("cs_");
                clientSecrect.append(organisationIdHash).append("_");
                clientSecrect.append(instant.toEpochMilli());
                String clientScrectHash = hashGenrator.Sha256Hashing(clientSecrect.toString());

                ClientApp newClientApp = ClientApp.builder()
                                                    .clientId("cli_"+organisationIdHash)
                                                    .clientSecretHash(clientScrectHash)
                                                    .organizationID(organisationId)
                                                    .createdBy(authenticatedUser)
                                                    .createdAt(instant)
                                                    .build();

                clientRepository.save(newClientApp);

                return new ClientAuthResponse("cli_"+organisationIdHash, clientSecrect.toString(), true);


            }
            else {
                throw new RuntimeException("Action not permitted");
            }
        }
        catch(NoSuchAlgorithmException e) {
           throw new RuntimeException("Action not permitted");
        }

    }


    public boolean isValidCredentials(ClientCredentialRequest clientCredentialRequest) throws NoSuchAlgorithmException {
        String clientScrectHash = hashGenrator.Sha256Hashing(clientCredentialRequest.clientSecret());
        return clientRepository.findByClientIdAndClientSecretHash(clientCredentialRequest.clientId() , clientScrectHash).isPresent();
    }

    public boolean isAlreadyCredentialsExist(String organisationId, String authenticatedUser) {
      
        boolean isallowed = organisationService.isAdminUserByEmail(organisationId, authenticatedUser);
        if(isallowed) {
            return clientRepository.findByOrganizationID(organisationId).isPresent();
        }
        else {
            throw new RuntimeException("Action not permitted");
        }
    }

    public boolean deleteClientCredentails(String organisationId, String authenticatedUser) {
        if(!isAlreadyCredentialsExist(organisationId, authenticatedUser)) {
            throw new RuntimeException("NOT DATA");
        }
        else {
            clientRepository.deleteByOrganizationID(organisationId);

            return true;
        }
    }

    public ClientCredentialResponse genrateToken(ClientCredentialRequest clientCredentialRequest) throws NoSuchAlgorithmException {
        if(isValidCredentials(clientCredentialRequest)) {
            String accessToken = clientTokenService.genrateClientToken(clientCredentialRequest.clientId());
            return new ClientCredentialResponse(accessToken, Duration.ofHours(2));
        }
        else {
            throw new RuntimeException("Not valid Credentials");
        }
    }
}
