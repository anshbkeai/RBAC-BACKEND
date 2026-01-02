package com.rdbac.rdbac.ServiceImplementation;

import com.rdbac.rdbac.Dto.AuthDto;
import com.rdbac.rdbac.Pojos.App_User;
import com.rdbac.rdbac.Repositry.App_User_Repositry;
import com.rdbac.rdbac.Service.AuthService;
import com.rdbac.rdbac.audit.domain.model.Audit;
import com.rdbac.rdbac.exceptions.AuthenticationFailedException;
import com.rdbac.rdbac.exceptions.UserAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service // here we are  using about the custom logs we nned to optimise about this also
public class AuthServiceImplementation implements AuthService {

    private App_User_Repositry appUserRepositry;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JWTServiceImplementation jwtServiceImplementation;

    public AuthServiceImplementation(App_User_Repositry appUserRepositry,
                                     AuthenticationManager authenticationManager,
                                     PasswordEncoder passwordEncoder,
                                    JWTServiceImplementation jwtServiceImplementation) {
        this.authenticationManager = authenticationManager;
        this.appUserRepositry = appUserRepositry;
        this.passwordEncoder = passwordEncoder;
        this.jwtServiceImplementation = jwtServiceImplementation;
    }
    /**
     * Authenticates a user with provided credentials using JWT-based authentication.
     * 
     * @param authDto Data transfer object containing user's email and password
     * @return JWT token if authentication is successful
     * @throws RuntimeException if authentication fails
     */
    @Override
    @Audit(
        action = "USER_LOGIN",
        targetType = "USER"
    )
    public String customAuth(AuthDto authDto) {
        log.info("User Attempts to log {} ", authDto.getEmail());
        Authentication authentication =  authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(authDto.getEmail(),authDto.getPassword())
                    );
        log.warn("User is {}", authentication.isAuthenticated());

        if(!authentication.isAuthenticated()) throw  new AuthenticationFailedException();

        log.warn("User is {}", authentication.isAuthenticated());
        return jwtServiceImplementation.genrateJwtToken(authDto.getEmail());
    }


    /**
     * Handles user registration (signup) process.
     * 
     * This method creates a new user account with basic information (email and password).
     * Additional user information can be updated later through a separate form/process.
     * 
     * The method performs the following steps:
     * 1. Checks if user already exists with the given email
     * 2. Creates new user with basic details
     * 3. Encrypts the password before storing
     * 4. Generates a unique UUID for user identification
     * 5. Saves the user to the database
     * 
     * @param authDto Data transfer object containing user's email and password for registration
     * @return JWT token generated for the newly registered user
     * @throws RuntimeException if a user already exists with the provided email
     */
    @Override
    @Audit(
        action = "USER_SIGNUP",
        targetType = "USER"
    )
    public String signup(AuthDto authDto) {
        // this si about  the  signip
        Optional<App_User> appUser  =  appUserRepositry.findByEmail(authDto.getEmail());
        if(appUser.isPresent()) throw new UserAlreadyExistsException();
        App_User appUser1 = new App_User();
        appUser1.setEmail(authDto.getEmail());
        appUser1.setPassword(passwordEncoder.encode(authDto.getPassword()));
        appUser1.setUser_id(UUID.randomUUID().toString());
        appUser1.setDate_joined(new Date());
        
        // woh. will save in the data abse 

        appUserRepositry.save(appUser1);
        return jwtServiceImplementation.genrateJwtToken(authDto.getEmail());
    }
}
