package com.rdbac.rdbac.ApiKey.core.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class ApiKey {

    @Id
    private String hash_api_key;

    @Indexed(unique = true)
    private String orgId;
    private Date created_at;
    private String created_by_user_email;

    //we are about the. api -key will be only one 

    // hey we are making about that no that changes about in teh org_repo .  
    // if is the valid about that not hitting about the muilitper repos as we can go for it is easy about to go. 
    
}
