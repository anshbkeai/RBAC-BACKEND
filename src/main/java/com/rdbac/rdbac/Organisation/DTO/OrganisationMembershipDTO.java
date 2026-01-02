package com.rdbac.rdbac.Organisation.DTO;


import java.util.Date;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganisationMembershipDTO {

    private String org_id;
    private String user_id;
    private Set<String> roles;
    private Set<String> permission;
    private Date added_at;
}
