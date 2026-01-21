package com.rdbac.rdbac.Role_Permission.application.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateDto {

    private String organisationId;
    private String roleId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<String> permissions;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String roleName;
}
