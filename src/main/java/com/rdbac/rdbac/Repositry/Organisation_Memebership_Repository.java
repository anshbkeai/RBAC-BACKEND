package com.rdbac.rdbac.Repositry;

import com.rdbac.rdbac.Pojos.Org_memberships;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface Organisation_Memebership_Repository extends MongoRepository<Org_memberships,String> {

    @Query("{org_id : ?0, user_id : ?1}")
    Org_memberships findByOrg_idandfindByUser_id(String org_id, String user_id);

    @Query("{user_id : ?0}")
    List<Org_memberships> findByUser_id(String user_id);

    @Query(value = "{ 'org_id': ?0 }", delete = true)
    void deleteAllByOrg_id(String org_id);
}
