package com.rdbac.rdbac.Email_Invite.application.dtop;

import com.rdbac.rdbac.Email_Invite.domain.model.Email_Invitation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailQueueDto {

    private Email_Invitation emailInvitation;
    private String token;
}
