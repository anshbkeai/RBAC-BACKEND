package com.rdbac.rdbac.Organisation.Service;

import org.springframework.stereotype.Service;

import com.rdbac.rdbac.Pojos.Organisation;
import com.rdbac.rdbac.Repositry.Organisation_Memebership_Repository;
import com.rdbac.rdbac.Repositry.Organisation_Repositry;
import com.rdbac.rdbac.ServiceImplementation.App_User_Core_ServiceImplementaion;
import com.rdbac.rdbac.exceptions.InvalidRolePermissionConfigException;
import com.rdbac.rdbac.exceptions.OrganizationNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationCommandService {

    private  final Organisation_Repositry organisation_Repositry;
    private final Organisation_Memebership_Repository organisation_Memebership_Repository;
    private final App_User_Core_ServiceImplementaion app_User_Core_ServiceImplementaion;

    public void deleteOrganization(String orgId, String deletingUserEmail) {
       /*
        1. Delete the Org membership .access_Check_Rest_Contoller
        abd every user hvaing in the org must be deleted that id . 
        // adn points is about that. like delete all of them and bedore that 
        // and that deletron procsss is a heavy 
       */
       Organisation organisation =  organisation_Repositry.findById(orgId).orElseThrow(() -> new OrganizationNotFoundException("Organsiation not found"));
       // now i have to delet it 

       if(!organisation.getCreated_by_user_id().equals(app_User_Core_ServiceImplementaion.getAppUserIdByEmail(deletingUserEmail)) ){
        throw new InvalidRolePermissionConfigException("You donot have the acccess");
       }
       organisation_Memebership_Repository.deleteAllByOrg_id(orgId);

       organisation.getUser_id_registred().stream()
                    .forEach(user -> app_User_Core_ServiceImplementaion.deleteUserOrganisation(user, orgId));
        organisation_Repositry.deleteById(orgId);
       
    }
}
