package com.rdbac.rdbac.ServiceImplementation;

import com.rdbac.rdbac.Pojos.App_User;
import com.rdbac.rdbac.Repositry.App_User_Repositry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImplementation implements UserDetailsPasswordService , UserDetailsService {
    private App_User_Repositry appUserRepositry;
    public  UserServiceImplementation(App_User_Repositry appUserRepositry) {
        this.appUserRepositry= appUserRepositry;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<App_User> find_user = appUserRepositry.findByEmail(username);
        if(!find_user.isPresent()) {
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
        return  find_user.get();
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}
