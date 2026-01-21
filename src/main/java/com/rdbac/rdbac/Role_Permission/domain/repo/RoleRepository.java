package com.rdbac.rdbac.Role_Permission.domain.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rdbac.rdbac.Role_Permission.domain.model.Role;

@Repository
public interface RoleRepository  extends MongoRepository<Role,String>{

    Role findBySlugAndOrgId(String slug, String orgId);
    java.util.List<Role> findByOrgId(String organisationId);
}
