package com.rdbac.rdbac.Role_Permission.domain.model;


import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class Role {

    @Id
    private String roleId;
    private String slug;
    private String roleName;
    private String orgId;
    private Set<String> permissions;
    private boolean customAllowed;

}
