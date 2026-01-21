package com.rdbac.rdbac.Role_Permission.interfaces.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.Role_Permission.application.dto.RoleCreateDto;
import com.rdbac.rdbac.Role_Permission.application.service.RoleCoreService;
import com.rdbac.rdbac.Role_Permission.domain.model.Role;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleRestRoute {

    private final RoleCoreService roleCoreService;

    private String getAuthUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PutMapping("/update")
    public ResponseEntity<String> createRole(@RequestBody RoleCreateDto roleCreateDto) {
    // how to update then is the simple . 
    // role Create -> create it -> org me custom role me nam     
        return new ResponseEntity<>(roleCoreService.updateRole(roleCreateDto , getAuthUser()), org.springframework.http.HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{orgId}")
    public ResponseEntity<List<Role>> getRolesByOrganisationId(@PathVariable String orgId) {
        return new ResponseEntity<>(roleCoreService.getRoleByOrganisationId(orgId), org.springframework.http.HttpStatus.OK);
    }
    
    
}
