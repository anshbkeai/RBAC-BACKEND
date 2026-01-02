package com.rdbac.rdbac.Email_Invite.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.rdbac.rdbac.Email_Invite.domain.model.Email_Invitation;
import java.util.List;
import java.util.Optional;


@Repository
public interface Email_Invitation_Repository extends MongoRepository<Email_Invitation , String> {

    Email_Invitation findByUserRecivedEmail(String userRecivedEmail);
    Optional<Email_Invitation> findByInvitationId(String invitationId);

    @Query("{organisation_id : ?0, userRecivedEmail : ?1}")
    Optional<Email_Invitation>  findByOrganisation_idandfindByUserRecivedEmail(String organisation_id , String userRecivedEmail);
}
