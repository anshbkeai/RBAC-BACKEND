package com.rdbac.rdbac.Role_Permission.application.dto;

import java.util.Set;

import lombok.Data;

@Data
public class RoleRquestDto {

    private String user_email_to_assign; // whom to Assign about that in the perticular user we c
    private String organisation_id;
    private Set<String> roles_assigned;
    private Set<String> permission_assigned;
    

}
