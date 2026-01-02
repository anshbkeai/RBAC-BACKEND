package com.rdbac.rdbac.Email_Invite.domain.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Document
@Data
@Getter
@Setter
public class Email_Invitation {


    
    @Id
    private String id;
    private String invitationId;
    private String organisation_id;
    private String user_sent_email;
    
    private String userRecivedEmail;
    private Date date_sent;
    private Date date_accepted;
    private Invite_Stauts stauts = Invite_Stauts.PENDING;
    
}
