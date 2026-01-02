package com.rdbac.rdbac.RestControllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.Dto.AuthDto;
import com.rdbac.rdbac.ServiceImplementation.AuthServiceImplementation;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // we  will authicatie the  means about signup and login and logout request must be  followd here

    private final AuthServiceImplementation authServiceImplementation;

    public  AuthController(AuthServiceImplementation authServiceImplementation) {
        this.authServiceImplementation = authServiceImplementation;
    }
    @PostMapping("/signup")
    public ResponseEntity<Map<String,String>> appUserSignup(@RequestBody AuthDto authDto) {
        return new ResponseEntity<Map<String,String>>(Map.of("token", authServiceImplementation.signup(authDto)), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> appUserLogin(@RequestBody  AuthDto authDto) {
       return new ResponseEntity<Map<String,String>>(Map.of("token", authServiceImplementation.customAuth(authDto)), HttpStatus.OK);
        
    }
    

}
