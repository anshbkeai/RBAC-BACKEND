package com.rdbac.rdbac.Repositry;

import com.rdbac.rdbac.Pojos.Organisation;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface Organisation_Repositry extends MongoRepository<Organisation,String> {

    List<Organisation> findByCreatedByUserId(String createdByUserId);
}
