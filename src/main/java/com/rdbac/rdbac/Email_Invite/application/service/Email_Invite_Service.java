package com.rdbac.rdbac.Email_Invite.application.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbac.rdbac.Email_Invite.application.dtop.EmailQueueDto;
import com.rdbac.rdbac.Email_Invite.application.dtop.Email_Invite_DTO;
import com.rdbac.rdbac.Email_Invite.domain.model.Email_Invitation;
import com.rdbac.rdbac.Email_Invite.domain.model.Invite_Stauts;
import com.rdbac.rdbac.Email_Invite.domain.repository.Email_Invitation_Repository;
import com.rdbac.rdbac.Organisation.Service.OrganisationService;
import com.rdbac.rdbac.Pojos.App_User;
import com.rdbac.rdbac.ServiceImplementation.App_User_Core_ServiceImplementaion;
import com.rdbac.rdbac.audit.domain.model.Audit;
import com.rdbac.rdbac.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Email_Invite_Service {

    //
    public final App_User_Core_ServiceImplementaion appUserCoreServiceImplementaion;
    public final Email_Invitation_Repository emailInvitationRepository;
    public final InviteJWTSerivce inviteJWTSerivce;
    private final RabbitTemplate rabbitTemplate;
    private final OrganisationService organisationService;
    private final ObjectMapper objectMapper;

    @Qualifier("email.exchange")
    private  TopicExchange topicExchange;

    public Email_Invite_Service(App_User_Core_ServiceImplementaion appUserCoreServiceImplementaion,
                               Email_Invitation_Repository emailInvitationRepository,
                               InviteJWTSerivce inviteJWTSerivce,
                               RabbitTemplate rabbitTemplate,
                               OrganisationService organisationService,
                               ObjectMapper objectMapper,
                               @Qualifier("email.exchange") TopicExchange topicExchange
                               
                               ) {
        this.appUserCoreServiceImplementaion = appUserCoreServiceImplementaion;
        this.emailInvitationRepository = emailInvitationRepository;
        this.inviteJWTSerivce = inviteJWTSerivce;
        this.rabbitTemplate = rabbitTemplate;
        this.organisationService = organisationService;
        this.objectMapper = objectMapper;
        this.topicExchange = topicExchange;
    }

    // constructure. left

    @Transactional
    @Audit(
        action = "EMAIL_QUEUED",
        targetType = "EMAIL",
        orgId = "#p0.organisationId"
    )
    public String sendEmailInvite(Email_Invite_DTO emailInviteDTO, String userEmail ) {
        // find about of the invied user existedin the app  // Bhai BSDK YE userEmail kya ka mc . ha Ghnata sMAJN nahi aa rha ha 
        // use about the App User cORE SERVICE

        //log.info(email_Invite_DTO.toString());

        if(!organisationService.canSendEmail(userEmail,emailInviteDTO.getOrganisationId())) {
            return  "You Donot Have Permission to Send the Email";
        }

        App_User invitedUser =  appUserCoreServiceImplementaion.Return_User_Exist(emailInviteDTO.getInvitedUserEmail());
       if(invitedUser == null) {
        // so about that if not prenset then we can about send to that Stauts is about pending to login , and ask for the Login 
            throw new UserNotFoundException("Target user does not exist"); 
       }
       
       if( (invitedUser.getOrgaisationId() != null &&invitedUser.getOrgaisationId().contains(emailInviteDTO.getOrganisationId()))){
        return "User Already in. the Organisation ";
       }

       Email_Invitation emailInvitation;
       Optional<Email_Invitation> email =emailInvitationRepository.
                                                    findByOrganisation_idandfindByUserRecivedEmail(emailInviteDTO.getOrganisationId(), 
                                                                                                    emailInviteDTO.getInvitedUserEmail()
                                                                                                );
       if(!email.isPresent()  ){
             emailInvitation = new Email_Invitation();
            
            emailInvitation.setInvitationId(UUID.randomUUID().toString());
            emailInvitation.setDate_sent(new Date());
            emailInvitation.setOrganisation_id(emailInviteDTO.getOrganisationId());
            emailInvitation.setUser_sent_email(userEmail);
            emailInvitation.setUserRecivedEmail(emailInviteDTO.getInvitedUserEmail());
       }
       else {
            emailInvitation = email.get();
            if(emailInvitation.getStauts() == Invite_Stauts.ACCEPTED) return "User Already Accepted it ";
            emailInvitation.setInvitationId(UUID.randomUUID().toString());
       }
       
       
       emailInvitationRepository.save(emailInvitation);

       String inviteToken = inviteJWTSerivce.genrateJwtToken(emailInviteDTO, userEmail, emailInvitation.getInvitationId());

       
       
       EmailQueueDto emailQueueDto = new EmailQueueDto(emailInvitation, inviteToken);
       try {

        String emailQueueDtoJson = objectMapper.writeValueAsString(emailQueueDto);
        rabbitTemplate.convertAndSend(topicExchange.getName(), "email.sent", emailQueueDtoJson);
        log.info("Email Sent Publish to exchamge {} routing key email.sent" , topicExchange.getName());

    } catch (JsonProcessingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return "SENT";
       
      
    }

    @Transactional
    @Audit(
        action = "EMAIL_ACCEPT",
        targetType = "EMAIL"
    )
    public Boolean isValid(String token) {
        String invitation_id = inviteJWTSerivce.get_invitation_id(token);
        Optional<Email_Invitation> optional = emailInvitationRepository.findByInvitationId(invitation_id);
        log.info("invitation_id {} and Optional {}",invitation_id,optional.isPresent());


        if(!optional.isPresent()) return false;
        if(inviteJWTSerivce.validate(optional.get(), token) && optional.get().getStauts().equals(Invite_Stauts.PENDING)) {
            //now what to do // more about that the invite is about the Pneding Should be updatws
            // so about we nned to run the cron Job that will mark all of the Pending to expired 
            Email_Invitation email_Invitation = optional.get();
            email_Invitation.setStauts(Invite_Stauts.ACCEPTED);
            // now this use has been invited to the app now we wnat bout ot user to be added in about they repo
            // orgisation repo 
            // orgsaiont 
           // organisationService.add_user();
            emailInvitationRepository.save(email_Invitation);
            appUserCoreServiceImplementaion.Add_Organisation_to_User(email_Invitation.getUserRecivedEmail(), email_Invitation.getOrganisation_id());
            organisationService.Add_User_to_Organisation(email_Invitation.getOrganisation_id(), email_Invitation.getUserRecivedEmail());
            log.info("User {} Sucessfully Added to the Organisation via Email invite {}",email_Invitation.getUserRecivedEmail(),email_Invitation.getOrganisation_id());
            return true;
         }
         return false;
    }

    public List<String> findByEmailStartingWith(String keyword) {
        Pageable pageRequest = PageRequest.of(0, 10);
        return appUserCoreServiceImplementaion.findByEmailLike(keyword , pageRequest).stream()
                                                .map(user -> user.getEmail())
                                                .toList();
    }
}
