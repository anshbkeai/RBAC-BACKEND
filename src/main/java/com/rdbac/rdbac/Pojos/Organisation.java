package com.rdbac.rdbac.Pojos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@Document
public class Organisation {
    @Id
    private String org_id;

    private String Name;

    private String created_by_user_id;
    private List<String> custome_roles_Created;
    private List<String> custom_permission_Created;

    private Set<String> user_id_registred;

}
