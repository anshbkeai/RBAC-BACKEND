package com.rdbac.rdbac.ApiKey.core.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rdbac.rdbac.ApiKey.core.model.ApiKey;
import java.util.Optional;


@Repository
public interface ApiKeyRepo extends MongoRepository<ApiKey,String> {

    Optional<ApiKey> findByOrgId(String orgId);
}
