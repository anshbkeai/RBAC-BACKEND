package com.rdbac.rdbac;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends MongoRepository<User,String> {
}
