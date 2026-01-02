package com.rdbac.rdbac.ApiKey.core.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rdbac.rdbac.ApiKey.config.HashGenrator;
import com.rdbac.rdbac.ApiKey.core.dto.ApiKey_dto;
import com.rdbac.rdbac.ApiKey.core.model.ApiKey;
import com.rdbac.rdbac.ApiKey.core.repo.ApiKeyRepo;
import com.rdbac.rdbac.Organisation.Service.Organisation_Memership_Service;
import com.rdbac.rdbac.Pojos.App_User;
import com.rdbac.rdbac.ServiceImplementation.App_User_Core_ServiceImplementaion;
import com.rdbac.rdbac.audit.domain.model.Audit;
import com.rdbac.rdbac.exceptions.ApiKeyAlreadyGeneratedException;
import com.rdbac.rdbac.exceptions.ApiKeyPermissionDeniedException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiKey_service {

    // here about to. get the dto object and about the time tanegs

    private final App_User_Core_ServiceImplementaion app_User_Core_ServiceImplementaion;
    private final Organisation_Memership_Service organisation_Memership_Service;
    private final HashGenrator hashGenrator;
    private final ApiKeyRepo apiKeyRepo;

    public ApiKey_service(App_User_Core_ServiceImplementaion app_User_Core_ServiceImplementaion , 
                            Organisation_Memership_Service organisation_Memership_Service,
                            HashGenrator hashGenrator,
                            ApiKeyRepo apiKeyRepo
    ) {
        this.app_User_Core_ServiceImplementaion = app_User_Core_ServiceImplementaion;
        this.organisation_Memership_Service = organisation_Memership_Service;
        this.hashGenrator = hashGenrator;
        this.apiKeyRepo= apiKeyRepo;
    }

    // methiod to gernate aout the. api key 
    @Audit(
        action = "API-KEY-GENERATED",
        orgId = "#p0.orgid",
        targetType = "API_KEY",
        targetId = "#p0.orgid"
    )
   public String genrateApiKey(ApiKey_dto apiKey_dto, String user_email) throws NoSuchAlgorithmException {
    // 1. Log the intent
    log.info("Request received to generate API key for user [{}] in organization [{}]", user_email, apiKey_dto.getOrgid());

    // 2. Fetch user object

    // 3. Permission check
    boolean allowed = organisation_Memership_Service.is_user_roles_permmision_org(
            app_User_Core_ServiceImplementaion.getAppUserIdByEmail(user_email), apiKey_dto.getOrgid(), "ADMIN", null);

    if (allowed) {
        log.info("User [{}] for organization [{}] passed ADMIN permission check", user_email, apiKey_dto.getOrgid());

        if(isApiKeyGenrated(apiKey_dto , user_email)) throw new ApiKeyAlreadyGeneratedException();
        // 4. Hash organization ID
        String org_hash = hashGenrator.Sha256Hashing(apiKey_dto.getOrgid());
        log.debug("Generated SHA-256 hash of org ID [{}]: [{}]", apiKey_dto.getOrgid(), org_hash);

        // 5. Create API key string
        StringBuilder api_key = new StringBuilder();
        api_key.append("access_");
        api_key.append(UUID.randomUUID().toString().split("-")[0]+"_");
        api_key.append(org_hash+"_");
        api_key.append(UUID.randomUUID().toString().split("-")[0]);

        log.debug("Generated raw API key (before hashing, not logged for security)");

        // 6. Hash the final API key (for DB storage)
        String api_hash_key = hashGenrator.Sha256Hashing(api_key.toString());
        log.debug("Hashed API key generated for DB storage");

        // 7. Save hashed key
        save_api_hash(user_email, apiKey_dto.getOrgid(), api_hash_key);
        log.info("API key hash stored in DB for user [{}] and org [{}]", user_email, apiKey_dto.getOrgid());

        // 8. Return actual API key to the user
        log.info("API key successfully generated and returned to user [{}]", user_email);
        return api_key.toString();
    } else {
        // 9. Permission denied
        log.warn("Unauthorized attempt by user [{}] to generate API key for org [{}]", user_email, apiKey_dto.getOrgid());
        throw new ApiKeyPermissionDeniedException("Not allowed to create API key");

    }
}


    private void save_api_hash(String user_email,String org_id , String api_key_hash) {
        ApiKey apiKey = new ApiKey();
        apiKey.setCreated_at(new Date());
        apiKey.setHash_api_key(api_key_hash);
        apiKey.setOrgId(org_id);
        apiKey.setCreated_by_user_email(user_email);

        apiKeyRepo.save(apiKey);
        log.info("Api- key Genrated aboout {}" ,apiKey.toString());


    }

    public boolean isvalid_api_key(String api_key_hash) throws NoSuchAlgorithmException {
        // sample about that Get the api-key-for the user and you 
        String hased = hashGenrator.Sha256Hashing(api_key_hash);
        // Bug . Logggin about the Api-key in the lOgs 
        log.info("Api-key  by user {}. and Hashed {}", api_key_hash,hased);
        Optional<ApiKey> optional  = apiKeyRepo.findById(hased);
        return optional.isPresent();
    }


    public boolean isApiKeyGenrated(ApiKey_dto apiKey_dto, String user_email) {
        boolean allowed = organisation_Memership_Service.is_user_roles_permmision_org(
            app_User_Core_ServiceImplementaion.getAppUserIdByEmail(user_email), apiKey_dto.getOrgid(), "ADMIN", null);
        if(allowed) {
           return apiKeyRepo.findByOrgId(apiKey_dto.getOrgid()).isPresent();
        }
        else {
             throw new ApiKeyPermissionDeniedException("Not allowed to create API key");

        }

    }
}
