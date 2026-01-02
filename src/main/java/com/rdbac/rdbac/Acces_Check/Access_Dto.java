package com.rdbac.rdbac.Acces_Check;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class Access_Dto {

    private String user_email;
    private String org_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String permission;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role;

}
