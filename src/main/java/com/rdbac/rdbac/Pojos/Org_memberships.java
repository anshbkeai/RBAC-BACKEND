package com.rdbac.rdbac.Pojos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Document
public  class Org_memberships {
    @Id
    private String org_user_member_id;
    private String org_id;
    private String user_id;
    private Set<String> roles;
    private Set<String> permission;
    private Date added_at;

}