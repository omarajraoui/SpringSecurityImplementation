package com.omar.jwt.Service;

import com.omar.jwt.Entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountService accountService;

    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //when utilisateur saisi mdp username appel cette methode
        AppUser appUser=accountService.loadUserByUsername(username);
        //type de role granted authority (new user is spring's)
        Collection<GrantedAuthority> authorities =new ArrayList<>();
        appUser.getAppRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

        });
        //de type user et roles mais les autoritées qu'il a
        return new User(appUser.getUsername(),appUser.getPassword(),authorities);
    }
}
