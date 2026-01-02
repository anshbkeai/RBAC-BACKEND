package com.rdbac.rdbac.Pojos;

import com.rdbac.rdbac.Pojos.Enums.Login_Mode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Document
@Data
@Getter
@Setter
public class App_User  implements UserDetails {

    @Id
    private String user_id;
    private String name;
    private String password;
    private Date date_joined;
    private String email;
    private List<String> orgaisationId; // this measn that these the organisation Created .  by the User 

    private Login_Mode loginMode = Login_Mode.Self; // login mode for teh  user as if we are going to povide about the Oauth

    //
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { // so this is the main reasone we need to talk aboout 
        // Returning false will reject login even with correct credentials. that is the major caused that is happeing 
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
