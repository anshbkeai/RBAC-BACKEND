package com.rdbac.rdbac.Role_Permission.application.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rdbac.rdbac.Organisation.Service.OrganisationService;
import com.rdbac.rdbac.Organisation.Service.Organisation_Memership_Service;
import com.rdbac.rdbac.Pojos.App_User;
import com.rdbac.rdbac.Pojos.Organisation;
import com.rdbac.rdbac.Role_Permission.application.dto.RoleRquestDto;
import com.rdbac.rdbac.ServiceImplementation.App_User_Core_ServiceImplementaion;
import com.rdbac.rdbac.audit.domain.model.Audit;
import com.rdbac.rdbac.exceptions.ApiKeyPermissionDeniedException;
import com.rdbac.rdbac.exceptions.OrganizationNotFoundException;
import com.rdbac.rdbac.exceptions.UserNotFoundException;

@Service
public class Role_Service {
    private final App_User_Core_ServiceImplementaion app_User_Core_ServiceImplementaion;
    private final OrganisationService organisationService;
    private final Organisation_Memership_Service organisation_Memership_Service;
    private RoleCoreService roleCoreService;

    public Role_Service(App_User_Core_ServiceImplementaion app_User_Core_ServiceImplementaion,
                        OrganisationService organisationService,
                        Organisation_Memership_Service organisation_Memership_Service,
                        RoleCoreService roleCoreService
    ) {
        this.app_User_Core_ServiceImplementaion = app_User_Core_ServiceImplementaion;
        this.organisationService = organisationService;
        this.organisation_Memership_Service  = organisation_Memership_Service;
        this.roleCoreService = roleCoreService;
    }

    // CRUD operations are pending implementation. Focus on completing these functionalities.

    /**
     * TODO: Implement update functionality for role permissions
     * Note: Current implementation needs to handle null permissions in roleRequestDto
     * to prevent unintended permission overwrites
     */
    @Audit(
        action = "ROLE_PERMISSION_ASSIGN",
        orgId = "#p0.organisation_id",
        targetType = "ROLE_PERMISSION"
    )
    public void Assign_Role_Permission(RoleRquestDto roleRquestDto,String admin_user) {
        // about we will. check about ahveing about the dep
       App_User app_User =  app_User_Core_ServiceImplementaion.Return_User_Exist(roleRquestDto.getUser_email_to_assign());
        if(app_User == null) {
            throw new UserNotFoundException("Target user does not exist"); 
        }
        Organisation organisation = organisationService.return_organisation_exist(roleRquestDto.getOrganisation_id());
        if(organisation == null)  throw new OrganizationNotFoundException("Organization not found");

        // getting about the. assingin_user in about to the loggin modules that will be easy to do so. 
        //App_User assinging_user = app_User_Core_ServiceImplementaion.Return_User_Exist(admin_user);
        if(!organisation_Memership_Service.can_assign_role_permission(app_User_Core_ServiceImplementaion.getAppUserIdByEmail(admin_user), organisation.getOrg_id()))  {
            throw new ApiKeyPermissionDeniedException("Insufficient permission to assign roles/permissions");

        }
        Set<String> filterroleIds = roleCoreService.getRolesByIds(roleRquestDto.getRolesassignedId()).stream().map(role -> role.getRoleId()).collect(Collectors.toSet());

        // i. can assing  the permission. to. the. user so 
        // org_mer update
        // update that only 

        organisation_Memership_Service.update_role_permission(app_User.getUser_id(),organisation.getOrg_id() ,filterroleIds);


    }

    
}
