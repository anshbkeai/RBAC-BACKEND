package com.rdbac.rdbac.Service;

import java.util.List;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.rdbac.rdbac.Dto.AppUserResponseDto;
import com.rdbac.rdbac.Pojos.App_User;

public interface App_User_Core_Services {

    // 
    boolean CanCreateOrganisation(String email);
    void Add_Organisation_to_User(String email, String organisation_id);
    App_User Return_User_Exist(String email);

    AppUserResponseDto getUserProfile(String email);

    Slice<App_User> findByEmailLike(String keyword, Pageable pageable);

    void deleteUserOrganisation(String userEmail, String org_id);
} 
