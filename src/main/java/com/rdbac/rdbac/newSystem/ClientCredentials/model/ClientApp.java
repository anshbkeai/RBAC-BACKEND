package com.rdbac.rdbac.newSystem.ClientCredentials.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class ClientApp {

    @Id
    private String clientId;
    private String clientSecretHash;
    @Indexed(unique = true)
    private String organizationID;  
    private Instant createdAt;
    private String createdBy;
}
