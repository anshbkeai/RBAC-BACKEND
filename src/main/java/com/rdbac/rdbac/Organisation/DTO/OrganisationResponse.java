package com.rdbac.rdbac.Organisation.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class OrganisationResponse {

    
    private String id;
    private String Name;
    private List<String> custome_roles_Created;
    private List<String> custome_permission_Created;

    private String creator_user_id;

}
