package com.rdbac.rdbac.Organisation.Controller;

import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.Organisation.DTO.OrgainsationDto;
import com.rdbac.rdbac.Organisation.DTO.OrganisationResponse;
import com.rdbac.rdbac.Organisation.DTO.OrganisationUserRolePermissionDto;
import com.rdbac.rdbac.Organisation.Service.OrganisationService;
import com.rdbac.rdbac.Organisation.Service.OrganizationCommandService;
import com.rdbac.rdbac.Pojos.Organisation;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/organisation")

public class OrganisationController {

   
    private OrganisationService organisationService;
    private final OrganizationCommandService organizationCommandService;
    
    public OrganisationController(
               
                OrganisationService organisationService,
                OrganizationCommandService organizationCommandService

    ) {
           
            this.organisationService = organisationService;
            this.organizationCommandService = organizationCommandService;
    }
    // to create an
    private String getAppUser_Email() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            // we. can. log
            return authentication.getName();
        }
        else {
            throw new RuntimeErrorException(new Error());
        }
    }

    @PostMapping("/")
    public ResponseEntity<Organisation> Create_Organisation(@RequestBody OrgainsationDto entity) {
        //TODO: process POST request
        String user_email = getAppUser_Email();
        
                // now we need to handle about the logic at the end part 
            Organisation organisation_created = organisationService.Create_Organisation(entity, user_email);
            return new ResponseEntity<Organisation>(organisation_created, HttpStatus.CREATED);

       
        
    }

  

    // to displau about the Memebs iin th e Orgainsai`zont 
    // like the DashBoard api. so thingiin 
    @GetMapping("/{org_id}/info")
    public ResponseEntity<EntityModel<OrganisationResponse>> Get_info_Organisation(@PathVariable String org_id  ) {
       // fetch about that and a link to get out the and more i think about we nned to. that. 
       OrganisationResponse response = organisationService.get_info_org_if(org_id);
       EntityModel<OrganisationResponse> model = EntityModel.of(response);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
            OrganisationController.class).Get_All_Members(org_id)).withRel("members"));

        return new ResponseEntity<EntityModel<OrganisationResponse>>(model, HttpStatus.OK);
       
       
    }

    // you have not added about that Page here and more about that if 1000 memener fetching is fked . up  
    // first optimistion
    @GetMapping("/{org_id}/members")
    public ResponseEntity<Set<String>> Get_All_Members(@PathVariable String org_id ) {
        return organisationService.Get_All_Members(org_id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EntityModel<OrganisationResponse>>> All_List_Organsation_For_Dev() {
        List<EntityModel<OrganisationResponse>> list = organisationService.get_all().stream().map(org -> {
            EntityModel<OrganisationResponse> model = EntityModel.of(org);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
            OrganisationController.class).Get_All_Members(org.getId())).withRel("members"));
            return model;

        }).toList(); 

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{org_id}/members/{user}/roles-permissions")
    public ResponseEntity<OrganisationUserRolePermissionDto> getUserRoleAndPermission(@PathVariable String org_id , @PathVariable String user) {
        return ResponseEntity.ok(organisationService.getUserRoleAndPermission(org_id, user));
    }
    
    
    // Thinsg about that are left in here about the understaind of teh the Crud Operations 
    @DeleteMapping("/{org_id}")
    public ResponseEntity<String> deleteOrgById(@PathVariable String org_id){
        organizationCommandService.deleteOrganization(org_id,getAppUser_Email());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
   

}
