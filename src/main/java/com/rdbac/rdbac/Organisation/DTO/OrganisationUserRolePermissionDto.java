package com.rdbac.rdbac.Organisation.DTO;

import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrganisationUserRolePermissionDto {

    private String user;
    private Set<String> user_roles;
     private Set<String> user_permissions;
     private List<String> available_roles;
     private List<String> available_permissions;
     
}
