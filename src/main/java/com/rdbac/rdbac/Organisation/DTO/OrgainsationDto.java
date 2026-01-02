package com.rdbac.rdbac.Organisation.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class OrgainsationDto {

    // therse will 
    private String name;
    private boolean customAllowed;

    // if the then we. can ahve. them. else. not ways 
    @JsonInclude(JsonInclude.Include.NON_NULL) // this will make sure about that. if they are not passes still work for. the enviromnet
    private List<String> customRoles;
    
     @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> customPermissions;

    
}
