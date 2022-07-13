package com.omar.jwt;

import com.omar.jwt.Entities.AppUser;
import com.omar.jwt.Service.AccountService;
import com.omar.jwt.filters.JwtAuthenticationFilter;
import com.omar.jwt.filters.JwtAutorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class securityConfig extends WebSecurityConfigurerAdapter {


   private AccountService accountService ;

    public securityConfig(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //droit dacces
        //donner tout les droits
        //h2 utilise frames html qui ne sont pas securisé
        http.headers().frameOptions().disable() ;
        http.csrf().disable();
        //afficher form de login au cas ou pas de droit
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.formLogin();
//        pou acceder a toutes resources doit etre authentifie

        //login automatically added but if its another endpoint authenticate
        //then add it in authorized requests
        http.authorizeRequests().antMatchers("/refreshToken/**","/login/**").permitAll();
//        http.authorizeRequests().antMatchers(HttpMethod.POST,"/users/**").hasAuthority("ADMIN");
//        http.authorizeRequests().antMatchers(HttpMethod.GET,"/users/**").hasAuthority("USER");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new JwtAutorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            //when utilisateur saisi mdp username appel cette methode
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser appUser=accountService.loadUserByUsername(username);
                //type de role granted authority (new user is spring's)
                Collection<GrantedAuthority> authorities =new ArrayList<>();
                appUser.getAppRoles().forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

                });
                //de type user et roles mais les autoritées qu'il a
                return new User(appUser.getUsername(),appUser.getPassword(),authorities);
            }
        });

        //utilisateurs qui ont droit dacces
    }

    //objet de type authmanagerbean to pass it as an arg
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
