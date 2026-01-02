package com.rdbac.rdbac.Organisation.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rdbac.rdbac.Helpers.DefaultRoles;
import com.rdbac.rdbac.Organisation.DTO.OrgainsationDto;
import com.rdbac.rdbac.Organisation.DTO.OrganisationResponse;
import com.rdbac.rdbac.Organisation.DTO.OrganisationUserRolePermissionDto;
import com.rdbac.rdbac.Pojos.Org_memberships;
import com.rdbac.rdbac.Pojos.Organisation;
import com.rdbac.rdbac.Repositry.Organisation_Memebership_Repository;
import com.rdbac.rdbac.Repositry.Organisation_Repositry;
import com.rdbac.rdbac.ServiceImplementation.App_User_Core_ServiceImplementaion;
import com.rdbac.rdbac.audit.domain.model.Audit;
import com.rdbac.rdbac.exceptions.InvalidRolePermissionConfigException;
import com.rdbac.rdbac.exceptions.OrganizationCreationNotAllowedException;
import com.rdbac.rdbac.exceptions.OrganizationNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrganisationService {
// now we need to defina all the Methids to do up here to in order

    private Organisation_Repositry organisation_Repositry;
    private Organisation_Memebership_Repository organisation_Memebership_Repository;
    private App_User_Core_ServiceImplementaion app_User_Core_ServiceImplementaion;

    public OrganisationService(
        Organisation_Repositry organisation_Repositry,
        Organisation_Memebership_Repository organisation_Memebership_Repository,
        App_User_Core_ServiceImplementaion app_User_Core_ServiceImplementaion
    ) {
            this.organisation_Memebership_Repository = organisation_Memebership_Repository;
            this.organisation_Repositry = organisation_Repositry;
            this.app_User_Core_ServiceImplementaion = app_User_Core_ServiceImplementaion;
    }

    // create 
    /**
     * Creates a new organization and establishes initial membership settings.
     *
     * This method handles the creation of a new organization with the following steps:
     * 1. Validates if the user has permission to create an organization
     * 2. Handles custom roles and permissions setup
     * 3. Creates and persists the organization entity
     * 4. Establishes organization membership for the creator
     * 5. Updates user's organization associations
     *
     * @param orgainsationDto The DTO containing organization details including name, custom roles,
     *                        and permissions settings
     * @param user_created_email The email of the user creating the organization
     * @return The newly created Organisation entity
     * @throws RuntimeException If the user cannot create more organizations
     * @throws RuntimeException If custom roles/permissions are enabled but null values are provided
     *
     * @see Organisation
     * @see OrgainsationDto
     * @see Org_memberships
     */
    @Transactional
    @Audit(
        action = "ORGANIZATION_CREATED",
        orgId = "",
        targetType = "ORGANIZATION",
        targetId = "#p1"
    )
    public Organisation Create_Organisation(OrgainsationDto orgainsationDto , String user_created_email) {
        
        // now you need to check about that yes i can creayte bout. the. it then tell me okay 

        log.info(" Org dto {}" , orgainsationDto.toString());
        log.info("Got JSON: isCustomAllowed = {}", orgainsationDto.isCustomAllowed());
        if(!app_User_Core_ServiceImplementaion.CanCreateOrganisation(user_created_email)) {
            throw new OrganizationCreationNotAllowedException("User cannot create more organizations");
        }
        if(orgainsationDto.isCustomAllowed()) {
            if(orgainsationDto.getCustomRoles() == null || orgainsationDto.getCustomPermissions() == null) 
                throw new InvalidRolePermissionConfigException("customRoles and customPermissions must be provided when isCustomAllowed=true");
        }
        else {
            // they get import about the DEFAULT that we have in the system 
                orgainsationDto.setCustomRoles(new DefaultRoles().CustomRoles);
                orgainsationDto.setCustomPermissions(new DefaultRoles().CustomPermissions);

        }

        Organisation organisation = new Organisation();
        
        // the logic below. is going to be taken by the Mapper Layer and that ObjectMaaper , or the Model
        
        organisation.setOrg_id(UUID.randomUUID().toString());
        organisation.setName(orgainsationDto.getName());
        organisation.setCreated_by_user_id(app_User_Core_ServiceImplementaion.getAppUserIdByEmail(user_created_email));
        organisation.setCustome_roles_Created(orgainsationDto.getCustomRoles());
        organisation.setCustom_permission_Created(orgainsationDto.getCustomPermissions());

        organisation.setUser_id_registred(new HashSet<>(List.of(user_created_email)));

        log.info("Creating organisation with name: {}", organisation.getName());
        organisation_Repositry.save(organisation);


        // gthis login in teh Hand of the them Object Mapper to be go with that
        Org_memberships memberships = new Org_memberships();
        memberships.setOrg_id(organisation.getOrg_id());
        memberships.setOrg_user_member_id(UUID.randomUUID().toString());
        memberships.setAdded_at(new Date());
        memberships.setUser_id(app_User_Core_ServiceImplementaion.getAppUserIdByEmail(user_created_email)); // fixed the bug about the incomistey of the data about saving thr user created email and for totjer id that create the inconsisteny of the data 
        memberships.setRoles(new HashSet<>(List.of("ADMIN")));
        memberships.setPermission(new HashSet<>(List.of("ALL")));

        organisation_Memebership_Repository.save(memberships);

        //
        app_User_Core_ServiceImplementaion.Add_Organisation_to_User(user_created_email, organisation.getOrg_id());


        return organisation;
    }

    public OrganisationResponse get_info_org_if(String org_id) {
        // TODO Auto-generated method stub

        Organisation organisation = organisation_Repositry.findById(org_id).get();
        OrganisationResponse organisationResponse = new OrganisationResponse();
        organisationResponse.setCreator_user_id(organisation.getCreated_by_user_id());
        organisationResponse.setName(organisation.getName());
        
        organisationResponse.setCustome_roles_Created(organisation.getCustome_roles_Created());
        organisationResponse.setCustome_permission_Created(organisation.getCustom_permission_Created());
        organisationResponse.setId(org_id);

        return organisationResponse;
    }

    /**
     * This method retrieves all registered member emails for a given organization.
     * It returns a ResponseEntity containing a set of member emails.
     * 
     * @param org_id The ID of the organization for which to retrieve member emails.
     * @return ResponseEntity containing a set of member emails.
     */
    public ResponseEntity<Set<String>> Get_All_Members(String org_id) {
         Set<String> memberEmails =  organisation_Repositry.findById(org_id).get().getUser_id_registred();
       
        
        return ResponseEntity.ok(memberEmails);
    }

    public List<OrganisationResponse> get_all() {
        return organisation_Repositry.findAll().stream()
            .map(org -> {
            OrganisationResponse response = new OrganisationResponse();
            response.setId(org.getOrg_id());
            response.setCreator_user_id(org.getCreated_by_user_id());
            response.setName(org.getName());
            response.setCustome_roles_Created(org.getCustome_roles_Created());
            response.setCustome_permission_Created(org.getCustom_permission_Created());
            return response;
            })
            .toList();
    }
    
    // method for adding user to the organsaiot
    /**
     * 
     * @param organisation_id
     * @param user_id
     */
    @Transactional
    @Audit(
        action = "ADD_USER_ORGANISATION",
        orgId = "#p0",
        targetType = "ORGANISATION",
        targetId = "#p1"
    )
    public void Add_User_to_Organisation(String organisation_id, String user_id) {
        Organisation organisation = return_organisation_exist(organisation_id) ;
        if(organisation != null) {
            organisation.getUser_id_registred().add(user_id);
            organisation_Repositry.save(organisation);
        }    
    }

    public Organisation return_organisation_exist(String organisation_id) {
        return organisation_Repositry.findById(organisation_id).isPresent()?organisation_Repositry.findById(organisation_id).get() : null; 
    }

    public boolean canSendEmail(String userEmail, String organisationId) {
       
         Org_memberships orgmemberships= organisation_Memebership_Repository.findByOrg_idandfindByUser_id(organisationId,app_User_Core_ServiceImplementaion.getAppUserIdByEmail(userEmail));
        // main question about is that. can:permission
        // Role Admin, 

       
        if(orgmemberships == null) return false;
        return orgmemberships.getRoles().contains("ADMIN")  ;
    }


    public String orgNameById(String organisationId){
        return organisation_Repositry.findById(organisationId)
            .map(Organisation::getName)
            .orElse(null);
    }

    /*To-do 
    Need to validate the user ifd and that stuff 
     */
    public OrganisationUserRolePermissionDto getUserRoleAndPermission(String org_id , String user) {
        Org_memberships memberships = organisation_Memebership_Repository.findByOrg_idandfindByUser_id(org_id,app_User_Core_ServiceImplementaion.getAppUserIdByEmail(user));
        Organisation organisation = organisation_Repositry.findById(org_id).orElseThrow();
        return OrganisationUserRolePermissionDto.builder()
                                                .user(user)
                                                .user_permissions(memberships != null  ? memberships.getPermission() : new HashSet<>())
                                                .user_roles(memberships != null  ? memberships.getRoles() : new HashSet<>())
                                                .available_roles(organisation.getCustome_roles_Created())
                                                .available_permissions(organisation.getCustom_permission_Created())
                                                .build()
                                                ;
    }
}
