package com.rdbac.rdbac.Acces_Check;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.ApiKey.core.service.ApiKey_service;

import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/access")
@Slf4j
public class Access_Check_Rest_Contoller {

    private final Access_Service access_Service;
    private final ApiKey_service apiKey_service;
    public Access_Check_Rest_Contoller(Access_Service access_Service,
                                        ApiKey_service apiKey_service
    ) {
        this.access_Service = access_Service;
        this.apiKey_service = apiKey_service;
    }

// REVIEW (Post-MVP / Production Hardening):
// ------------------------------------------------------------
// ⚠️ This endpoint relies solely on an API key for access validation. 
// In production, it’s likely to be hit frequently by external services or automated clients.
//
// ✅ Recommended Enhancements:
// 1️⃣ **Rate Limiting:** Implement request throttling (e.g., Bucket4j, Spring Cloud Gateway filters, or API Gateway policies)
//     to prevent abuse or accidental request floods from the same API key or IP.
// 2️⃣ **API Key Management:** 
//     - Store issued keys securely (hashed, not plaintext).
//     - Add metadata: owner, rate limit, expiration, and usage count.
//     - Support key revocation and rotation.
// 3️⃣ **Telemetry & Monitoring:** Log API key usage counts (not raw keys) for audit and analytics.
// 4️⃣ **Error Handling:** Return 429 (Too Many Requests) when rate limits are exceeded instead of 400/403.
//
// 💡 MVP Note:
// Current validation via `apiKey_service.isvalid_api_key()` is fine for now,
// but a production-grade system should centralize rate limiting and key lifecycle management.

    @PostMapping("/check") // i need about the Header apbout to gewt /
    public ResponseEntity<Map<String,Boolean>> AccessCheck(@RequestBody Access_Dto access_Dto , @RequestHeader("X-API-KEY") String api_key) throws NoSuchAlgorithmException {
        Map<String,Boolean> map = new HashMap<>();
        if(!apiKey_service.isvalid_api_key(api_key))  { map.put("isallowed", false); return new ResponseEntity<Map<String,Boolean>>(map, HttpStatus.BAD_REQUEST);}

        map.put("isallowed",access_Service.isallowed(access_Dto));
        
        return new ResponseEntity<Map<String,Boolean>>(map, HttpStatus.OK);
    }
    
}
