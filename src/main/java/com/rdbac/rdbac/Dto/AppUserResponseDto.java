package com.rdbac.rdbac.Dto;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserResponseDto {

    // what are the things.i need in the response 
    // Email 
    
    private Date dateJoined;
    private String email;
    private String userId;
    private List<String> orgaisationId;
}
