package com.rdbac.rdbac.Role_Permission.interfaces.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.Role_Permission.application.dto.RoleRquestDto;

import com.rdbac.rdbac.Role_Permission.application.service.Role_Service;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/roles/permission")
@Slf4j
public class Roles_Permission_Rest_routes {

    private final Role_Service role_Service;
    public Roles_Permission_Rest_routes(Role_Service role_Service ) {
        this.role_Service = role_Service;
    }
    
    public String getMethodName() {
        return "Test";
    }
    
    @PutMapping("/assign")
    public ResponseEntity<Map<String,Object>> assignRoleAndPermission(@RequestBody RoleRquestDto  roleRquestDto) {
       
        // about what you will do is simple bout that you will add them to us in about the route in the layman terms 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("The Auth Object. is. "+authentication.getName());
        role_Service.Assign_Role_Permission(roleRquestDto, authentication.getName());
        log.info(roleRquestDto.toString());
        return new ResponseEntity<Map<String,Object>>(new HashMap<>(Map.of("message", "Assgined Role and Permisson")), HttpStatus.OK);
    }
    
}
