package com.omar.jwt;

import com.omar.jwt.Service.AccountService;
import com.omar.jwt.Service.UserDetailsServiceImpl;
import com.omar.jwt.filters.JwtAuthenticationFilter;
import com.omar.jwt.filters.JwtAutorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class securityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

   private AccountService accountService ;

    public securityConfig(UserDetailsServiceImpl userDetailsService, AccountService accountService) {
        this.userDetailsService = userDetailsService;
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

        auth.userDetailsService(userDetailsService);


    }

    //objet de type authmanagerbean to pass it as an arg
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
