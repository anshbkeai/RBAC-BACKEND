package com.rdbac.rdbac.Repositry;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rdbac.rdbac.Pojos.Review;
@Repository
public interface ReviewRepository  extends MongoRepository<Review,ObjectId>{

}
