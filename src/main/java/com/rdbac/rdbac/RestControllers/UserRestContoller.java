package com.rdbac.rdbac.RestControllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.Dto.AppUserResponseDto;
import com.rdbac.rdbac.Organisation.DTO.OrganisationMembershipDTO;
import com.rdbac.rdbac.Organisation.DTO.OrganisationResponse;
import com.rdbac.rdbac.Organisation.Service.OrganisationService;
import com.rdbac.rdbac.Organisation.Service.Organisation_Memership_Service;
import com.rdbac.rdbac.Pojos.Org_memberships;
import com.rdbac.rdbac.ServiceImplementation.App_User_Core_ServiceImplementaion;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestContoller {
    private final App_User_Core_ServiceImplementaion appUserCoreServiceImplementaion;
    private final  OrganisationService organisationService;
    private final  Organisation_Memership_Service organisation_Memership_Service;
    
    @GetMapping("/profile")
    public ResponseEntity<AppUserResponseDto> getUserProfile() {
        return ResponseEntity.ok(appUserCoreServiceImplementaion.getUserProfile(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @GetMapping("/me/organisation")
    public ResponseEntity<List<OrganisationResponse>> getOrganisationCreatedByMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrganisationResponse > organisationResponses = appUserCoreServiceImplementaion.getUserProfile(email).getOrgaisationId().stream()
                                            .map(org -> organisationService.get_info_org_if(org))
                                            .toList();
        return  ResponseEntity.ok(organisationResponses);

    }

    /*
        feat: return user organization memberships as orgId-keyed map

Collected user organization memberships into a Map keyed by orgId to enable
O(1) access for permission and role resolution.

Currently assumes a single membership per user per organization.
This can be optimized later by:
- adding a merge strategy for duplicate orgIds, or
- switching to groupingBy if multiple memberships per org are required.

    */
    @GetMapping("/me/organisation/get")
    public ResponseEntity<Map<String , OrganisationMembershipDTO >> getOrganisationEnrolledByMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String , OrganisationMembershipDTO > map = organisation_Memership_Service.getuserMemberships(appUserCoreServiceImplementaion.getAppUserIdByEmail(email)).stream()
                    .collect(Collectors.toMap(org -> org.getOrg_id(),  org->organisationMembershipDTO(org)));
        return  ResponseEntity.ok(map);

    }

    private OrganisationMembershipDTO organisationMembershipDTO(Org_memberships memberships) {
        return OrganisationMembershipDTO.builder()
                                        
                                        .org_id(memberships.getOrg_id())
                                        .permission(memberships.getPermission())
                                        .roles(memberships.getRoles())
                                        .user_id(memberships.getUser_id())
                                        .added_at(memberships.getAdded_at())
                                        .build()
                                        ;
                                        
    }

    // need the api to fetch what is my role in the prt

    @GetMapping("/me/organisation/admin")
    public ResponseEntity<Map<String,String>> getOrganisationAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
       List<Org_memberships> orgMap =
                organisation_Memership_Service
                    .getuserMemberships(appUserCoreServiceImplementaion.getAppUserIdByEmail(email))
                    .stream()
                    .filter(m -> m.getRoles().contains("ADMIN"))
                    .toList();
        Map<String,String> map = new HashMap<>();
        orgMap.stream().forEach(x -> map.put(x.getOrg_id(), organisationService.orgNameById(x.getOrg_id())));
        return  ResponseEntity.ok(map);

    }

    
    
    
}
