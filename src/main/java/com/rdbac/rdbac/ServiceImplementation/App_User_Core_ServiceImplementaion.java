package com.rdbac.rdbac.ServiceImplementation;

import java.util.ArrayList;
import java.util.List;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rdbac.rdbac.Dto.AppUserResponseDto;
import com.rdbac.rdbac.Pojos.App_User;
import com.rdbac.rdbac.Repositry.App_User_Repositry;
import com.rdbac.rdbac.Service.App_User_Core_Services;
import com.rdbac.rdbac.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class App_User_Core_ServiceImplementaion implements App_User_Core_Services {

    private App_User_Repositry app_User_Repositry;
    public App_User_Core_ServiceImplementaion( App_User_Repositry app_User_Repositry

    )  {
            this.app_User_Repositry = app_User_Repositry;
    }


    @Override
    public boolean CanCreateOrganisation(String email) {
        
        List<String> organisation_created_by_user = app_User_Repositry.findByEmail(email).get().getOrgaisationId();
        return organisation_created_by_user == null || organisation_created_by_user.size() < 3;
    }


    /**
 * TODO: Establish a bidirectional association between a User and an Organization.
 * This method should:
 * - Update the User document to include the Organization ID.
 * - Update the Organization document to include the User ID.
 * 
 * Implementation is pending. Ensure atomicity and data consistency when updating both entities.
 */

    @Override
    public void Add_Organisation_to_User(String email, String organisation_id) {
        // TODO Auto-generated method stub
        // user document orgisation 
        // orginsation user add. 
        App_User app_user =  app_User_Repositry.findByEmail(email).orElseThrow();
        if(app_user.getOrgaisationId() == null) {
            app_user.setOrgaisationId(new ArrayList<>(List.of(organisation_id)));
        }
        else {
            List<String> organisation_created_by_user = app_user.getOrgaisationId();
            organisation_created_by_user.add(organisation_id);
            app_user.setOrgaisationId(organisation_created_by_user);
        }
        app_User_Repositry.save(app_user);

    }


    @Override
    public App_User Return_User_Exist(String email) {
        return app_User_Repositry.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }


    public String getAppUserIdByEmail(String email ) {
        App_User app_User= Return_User_Exist(email);
        return (app_User==null ? "" : app_User.getUser_id());
    }


    @Override
    public AppUserResponseDto getUserProfile(String email) {
        App_User app_User = app_User_Repositry.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return AppUserResponseDto.builder()
                                        .dateJoined(app_User.getDate_joined())
                                        .email(app_User.getEmail())
                                        .orgaisationId(app_User.getOrgaisationId())
                                        .userId(app_User.getUser_id())
                                        .build();
                                                                    
                                                                
        
    }


    @Override
    public Slice<App_User> findByEmailLike(String keyword, Pageable pageable) {

         return app_User_Repositry.findByEmailStartingWith(keyword,pageable);
    }


    @Transactional
    @Override
    public void deleteUserOrganisation(String userEmail, String org_id) {
       App_User app_User = app_User_Repositry.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));
        
        if(app_User.getOrgaisationId().contains(org_id)) app_User.getOrgaisationId().remove(org_id);

        app_User_Repositry.save(app_User);

        
    }


   


   

}
