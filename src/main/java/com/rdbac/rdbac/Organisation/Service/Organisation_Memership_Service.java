package com.rdbac.rdbac.Organisation.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rdbac.rdbac.Organisation.DTO.OrganisationMembershipDTO;
import com.rdbac.rdbac.Pojos.Org_memberships;
import com.rdbac.rdbac.Repositry.Organisation_Memebership_Repository;
import com.rdbac.rdbac.Role_Permission.application.service.RoleCoreService;
import com.rdbac.rdbac.Role_Permission.domain.model.Role;
import com.rdbac.rdbac.audit.domain.model.Audit;
import com.rdbac.rdbac.exceptions.InvalidRolePermissionRequestException;
import com.rdbac.rdbac.exceptions.MembershipNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing organisation memberships, roles, and permissions.
 * <p>
 * This service provides methods to add users to organisations, update their roles and permissions,
 * check if a user can assign roles/permissions, and verify if a user has specific roles or permissions
 * within an organisation.
 * </p>
 *
 * <p>
 * Typical usage:
 * <ul>
 *   <li>Add a user to an organisation with specific roles and permissions</li>
 *   <li>Update roles and permissions for an existing user in an organisation</li>
 *   <li>Check if a user has the authority to assign roles/permissions</li>
 *   <li>Verify if a user has a specific role or permission in an organisation</li>
 * </ul>
 * </p>
 *
 * <p>
 * Note: This service assumes that role and permission checks are handled via sets of strings,
 * and does not enforce any specific role/permission hierarchy or structure.
 * </p>
 *
 * @author ansh
 * @since 1.0
 */

@Service
@Slf4j
public class Organisation_Memership_Service {

    private final Organisation_Memebership_Repository organisation_Memebership_Repository;
    private final RoleCoreService roleCoreService;

    public Organisation_Memership_Service(Organisation_Memebership_Repository organisation_Memebership_Repository,

                                            RoleCoreService roleCoreService
    ) {
        this.organisation_Memebership_Repository = organisation_Memebership_Repository;
        this.roleCoreService = roleCoreService;
    }

    public Org_memberships get_org_user_Memberships(String user_id , String org_id) {
        return organisation_Memebership_Repository.findByOrg_idandfindByUser_id(org_id, user_id);
    }

    public List<Org_memberships> getuserMemberships(String user_id) {
        return organisation_Memebership_Repository.findByUser_id( user_id);
    }

    public List<OrganisationMembershipDTO> getuserMembershipsV1(String user_id) {
        return organisation_Memebership_Repository.findByUser_id( user_id).stream()
                .map(org -> organisationMembershipDTO(org)).toList();
    }

     private OrganisationMembershipDTO organisationMembershipDTO(Org_memberships memberships) {
        Set<String> roles = roleCoreService.getRolesByIds(memberships.getRolesId()).stream()
                                            .map(role -> role.getRoleName()).collect(Collectors.toSet());
        return OrganisationMembershipDTO.builder()
                                        
                                        .org_id(memberships.getOrg_id())
                                        .permission(memberships.getPermission())
                                        .roles(roles)
                                        .user_id(memberships.getUser_id())
                                        .added_at(memberships.getAdded_at())
                                        .build()
                                        ;
                                        
    }
    
    @Audit(
        action = "MEMBERSHIP_CREATED",
        orgId = "#p1",
        targetType = "ORGANISATION_MEMBERSHIP",
        targetId = "#p0"
    )
    public void add_user_to_membership(String user_id, String org_id, Set<String> rolesId) {
        Org_memberships org_memberships = new Org_memberships();
        org_memberships.setOrg_id(org_id);
        org_memberships.setUser_id(user_id);
        org_memberships.setRolesId(rolesId);
        org_memberships.setOrg_user_member_id(UUID.randomUUID().toString());
        org_memberships.setAdded_at(new Date());
        organisation_Memebership_Repository.save(org_memberships);
        log.info("Added user {} to org {} with roles {}", user_id, org_id, rolesId);
    }


    /**
 * Checks if a user has permission to assign roles within an organization.
 * Currently, this method only checks for ADMIN role privileges.
 * 
 * TODO: Future enhancement needed to consider permission-based access control
 * in addition to role-based access control.
 *
 * @param user_id The unique identifier of the user being checked
 * @param org_id The unique identifier of the organization
 * @return boolean Returns true if the user has ADMIN role, false otherwise
 *         Returns false if the user membership doesn't exist
 */
    public boolean can_assign_role_permission(String user_id, String org_id) {
        Org_memberships org_memberships= organisation_Memebership_Repository.findByOrg_idandfindByUser_id(org_id, user_id);
        // main question about is that. can:permission
        // Role Admin, 
        if(org_memberships == null) return false;
        return org_memberships.getRoles().contains("ADMIN")  ; // what if here is not admin but have the.  permission. to do so.  about how can i. 
        // till about we will not consied about then we. will about to handle about. that. in. the.  futute .  let. keep it simpe
    }

    /** 
     REVIEW (Post-MVP): This method mixes responsibilities (create + update) and lacks proper feedback to the client. 
     Refactor after MVP to: 
     1️ Separate CRUD operations clearly (addRole, deleteRole, addPermission, etc.).  
     2 Return a meaningful response instead of void (e.g., updated object or success flag).  
     3 Add validation and proper error handling for null/invalid data.  
     4 Improve naming (camelCase) and structured logging for better maintainability.
    */
   @Audit(
        action = "MEMBERSHIP_UPDATED",
        orgId = "#p1",
        targetType = "ORGANISATION_MEMBERSHIP",
        targetId = "#p0"
    )
    public void update_role_permission(String user_id, String org_id, Set<String> rolesId) {
        Org_memberships org_memberships= organisation_Memebership_Repository.findByOrg_idandfindByUser_id(org_id, user_id);
        
        // what if you are assing about the Means about this is the different api that we are means
        // about that. we have about   the that is the api to assing the user aboout that we should be assing
        // and we need also handle about the null values in that what if he exits 
       if(org_memberships == null) {
        //means about that user who is going to be addedin th e org does not exits 
            add_user_to_membership(user_id, org_id, rolesId);
            return;
       }
       
        //org_memberships.setRoles(new HashSet<>(roles));

       org_memberships.setRolesId(rolesId);
      //org_memberships.setPermission(new HashSet<>(permission));

       org_memberships.setAdded_at(new Date());

        // we donot have to think about the if assing about the. same permission that we are ar about using the Sets and aboout that 
        // so owerite and about we can limit in the fornetendf about handling in the. values. in the databse
        organisation_Memebership_Repository.save(org_memberships);
        
        log.info("We have successfully update about the. roles form {} in org {} ", user_id,org_id);
       


    }


    /**
 * Validates role and permission assignments to ensure data integrity.
 *
 * This method ensures that:
 * - No role or permission value is an empty string.
 * - An empty string should never be part of the role or permission set stored in the database.
 *
 * Allowing empty strings introduces ambiguity and may lead to unexpected access control behaviors.
 * This validation step helps enforce strict and predictable access logic by rejecting any invalid assignments.
 */
    public boolean is_user_roles_permmision_org(String user_id, String org_id,String requiredPermission) {
        log.info("Org id {} ,  user id {}" , org_id , user_id);
        Org_memberships org_memberships= organisation_Memebership_Repository.findByOrg_idandfindByUser_id(org_id, user_id);
        if(org_memberships == null) {
            throw new MembershipNotFoundException("Membership not found for user in organization");
        }

        List<Role> roles = roleCoreService.getRolesByIds(org_memberships.getRolesId());
        return roles.stream().flatMap(role -> role.getPermissions().stream()).collect(Collectors.toSet()).contains(requiredPermission);
        
    }


}
