package com.rdbac.rdbac.Acces_Check;

import org.springframework.stereotype.Service;

import com.rdbac.rdbac.Organisation.Service.Organisation_Memership_Service;

import com.rdbac.rdbac.ServiceImplementation.App_User_Core_ServiceImplementaion;

@Service
public class Access_Service {

    private final Organisation_Memership_Service organisation_Memership_Service;
    private final App_User_Core_ServiceImplementaion app_User_Core_ServiceImplementaion;

    public Access_Service(Organisation_Memership_Service organisation_Memership_Service,
                App_User_Core_ServiceImplementaion app_User_Core_ServiceImplementaion
    ) {
        this.organisation_Memership_Service =organisation_Memership_Service;
        this.app_User_Core_ServiceImplementaion= app_User_Core_ServiceImplementaion;
    }
    public boolean isallowed(Access_Dto access_Dto)  {
        String app_user_id = app_User_Core_ServiceImplementaion.getAppUserIdByEmail(access_Dto.getUser_email());
        return organisation_Memership_Service.is_user_roles_permmision_org(app_user_id, access_Dto.getOrg_id(),
                 (access_Dto.getRole()),
                 (access_Dto.getPermission()));
    }
}
