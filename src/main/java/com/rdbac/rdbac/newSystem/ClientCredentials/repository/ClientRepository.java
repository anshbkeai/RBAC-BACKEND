package com.rdbac.rdbac.newSystem.ClientCredentials.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rdbac.rdbac.newSystem.ClientCredentials.model.ClientApp;

import java.util.Optional;


public interface ClientRepository extends MongoRepository<ClientApp,String>{

    Optional<ClientApp> findByOrganizationID(String organizationID);

    Optional<ClientApp> findByClientIdAndClientSecretHash(String clientId , String clientSecretHash);

    void deleteByOrganizationID(String organizationID);
    
}
