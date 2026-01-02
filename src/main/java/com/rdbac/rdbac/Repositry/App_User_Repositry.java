package com.rdbac.rdbac.Repositry;

import com.rdbac.rdbac.Pojos.App_User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface App_User_Repositry  extends MongoRepository<App_User,String> {

    Optional<App_User> findByEmail(String email);

    Slice<App_User> findByEmailStartingWith(String prefix,Pageable pageable);

}
