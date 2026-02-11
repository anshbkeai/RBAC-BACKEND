package com.rdbac.rdbac.Role_Permission.application.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rdbac.rdbac.Organisation.Service.OrganisationService;
import com.rdbac.rdbac.Pojos.Organisation;
import com.rdbac.rdbac.Repositry.App_User_Repositry;
import com.rdbac.rdbac.Repositry.Organisation_Repositry;
import com.rdbac.rdbac.Role_Permission.application.dto.RoleCreateDto;
import com.rdbac.rdbac.Role_Permission.domain.model.Role;
import com.rdbac.rdbac.Role_Permission.domain.repo.RoleRepository;
import com.rdbac.rdbac.exceptions.InvalidRolePermissionConfigException;
import com.rdbac.rdbac.exceptions.OrganizationNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleCoreService {

    private final RoleRepository roleRepository;
    private final Organisation_Repositry organisation_Repositry;
    private final App_User_Repositry app_User_Repositry;

    @Transactional
    public String updateRole(RoleCreateDto createDto , String authUser) {
        Organisation organisation = organisation_Repositry.findById(createDto.getOrganisationId()).orElseThrow(() ->  new OrganizationNotFoundException("Not Found Organisation"));
        String appUserId = app_User_Repositry.findByEmail(authUser).orElseThrow().getUser_id();
        if(!organisation.getCreated_by_user_id().equals(appUserId)) {
            throw new InvalidRolePermissionConfigException("Only Organisation Creator can update role permissions ");
        }
        if(!organisation.getCustom_permission_Created().containsAll(createDto.getPermissions())) throw new InvalidRolePermissionConfigException("Permission  mismacth ");

        if(createDto.getPermissions() == null && (createDto.getRoleName().isBlank() || createDto.getRoleName() == null) ) {
            throw new InvalidRolePermissionConfigException("Invalid Config Provided "); 
        }
        Role role = roleRepository.findById(createDto.getRoleId()).orElseThrow(() -> new RuntimeException("Role Not Found Error "));
        role.getPermissions().addAll(createDto.getPermissions());
        
        
        roleRepository.save(role);
        return role.getRoleId();


    }

    @Transactional
    public String createRole(String roleName,String organisationId) {
      //  Organisation organisation = organisation_Repositry.findById(organisationId).orElseThrow(() ->  new OrganizationNotFoundException("Not Found Organisation"));
        Role role = Role.builder().orgId(organisationId)
                                    .permissions(new HashSet<>())
                                    .roleId("role-"+UUID.randomUUID().toString()).
                                    roleName(roleName)
                                    .slug(generateSlug(roleName))
                                    .build();
        roleRepository.save(role);      
        return role.getRoleId();
    }

       
    public String generateSlug(String title) {
        return title
            .toLowerCase()
            .trim()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("\\s+", "-");
    }

    public List<Role> getRoleByOrganisationId(String organisationId) {
        return roleRepository.findByOrgId(organisationId);
    }

    public List<Role> getRolesByIds(Set<String> roleIds) {
        if(roleIds == null || roleIds.isEmpty()) {
            return List.of();
        }
        return roleRepository.findAllById(roleIds);
    }
}
