package com.rdbac.rdbac.ApiKey.core.RestContoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.ApiKey.core.dto.ApiKey_dto;
import com.rdbac.rdbac.ApiKey.core.service.ApiKey_service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/apikey")
public class ApiKey_rest_controller {

    private final ApiKey_service apiKey_service;
    public ApiKey_rest_controller(ApiKey_service apiKey_service) {
        this.apiKey_service = apiKey_service;
    }

    // we will. have to genrate about 
    // to about what is  main part about that check/api key and in the access/check about we nned to
    // cheack about api-service - that is the main part 
    // that will comen he. 

    // this. we are creating aboout the key that are irretabe means about i nned in gthe post oabjetb
   
    private String get_Authenticated_User() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    @PostMapping("/genrate")
    public ResponseEntity<Map<String,String>> genrateApiKey(@RequestBody ApiKey_dto entity) throws NoSuchAlgorithmException {
       
        // get me 
        String user_email = get_Authenticated_User();
        String api_key = apiKey_service.genrateApiKey(entity, user_email);

        Map<String,String> map = new HashMap<>();
        map.put("apikey", api_key);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
        
    }

    /** isUserAdmin Routes nned to be careate about in here to mnake about or enduse about whom to show thos pahe or not 
     * Check whether an API key has been generated for the given organization.
     *
     * This endpoint verifies if an API key exists for the orgId provided inside the ApiKey_dto.
     *
     * @param entity ApiKey_dto containing the organization id (orgid)
     * @return ResponseEntity<Boolean> true if an API key exists for the org, false otherwise
     * access_5b6e99f3_54a627aa6ca2abd21e0d22ca1208b665c5aa356fa97ee9ae2c2a6694de70777c_2f864fe6
     */
    @GetMapping("/check/{orgid}")
    public  ResponseEntity<Map< String, Boolean>> isApiKeyGenrated(@PathVariable String orgid) {
        ApiKey_dto entity = new ApiKey_dto();
        entity.setOrgid(orgid);
        return new ResponseEntity<>(Map.of("check",apiKey_service.isApiKeyGenrated(entity , get_Authenticated_User())), HttpStatus.OK);
    }
    

    
}
