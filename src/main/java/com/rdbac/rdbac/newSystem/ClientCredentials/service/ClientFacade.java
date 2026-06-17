package com.rdbac.rdbac.newSystem.ClientCredentials.service;

import org.springframework.stereotype.Service;

import com.rdbac.rdbac.Acces_Check.Access_Dto;
import com.rdbac.rdbac.Acces_Check.Access_Service;
import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientAccessRequest;
import com.rdbac.rdbac.newSystem.ClientCredentials.dto.ClientAccessResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientFacade {

    private final Access_Service access_Service;

    public ClientAccessResponse verify(ClientAccessRequest accessRequest  ) {
        Access_Dto access_Dto = getAccessDto(accessRequest); 

        return new ClientAccessResponse(access_Service.isallowed(access_Dto));
    }

    public Access_Dto getAccessDto(ClientAccessRequest accessRequest ) {
        Access_Dto access_Dto = new Access_Dto();
        access_Dto.setOrg_id(accessRequest.organisationId());
        access_Dto.setUser_email(accessRequest.userEmail());
        access_Dto.setPermission(accessRequest.permission());
        return access_Dto;
    }
}
