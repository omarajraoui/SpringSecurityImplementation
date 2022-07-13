package com.omar.jwt.Web;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omar.jwt.Entities.AppRole;
import com.omar.jwt.Entities.AppUser;
import com.omar.jwt.JWTUtil;
import com.omar.jwt.Service.AccountService;
import lombok.Data;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AccountRestController {

    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/users")
    @PostAuthorize("hasAuthority('USER')")
    public List<AppUser> appUsers (){
        return accountService.listUsers();

    }


    @PostMapping(path = "/users")
    @PostAuthorize("hasAuthority('ADMIN')")
    //@Requestbody les donnes de appuser se retrouve dans le coeur de requete
    public AppUser saveUser(@RequestBody AppUser appUser){
        //it automatically encodes the pw
        return accountService.addNewUser(appUser);

    }

    @GetMapping(path = "/roles")
    public List<AppRole> appRoles(){
        //it automatically encodes the pw
        return accountService.listRoles();

    }

    @PostMapping(path = "/roles")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppRole saveRole(@RequestBody AppRole appRole){
        //it automatically encodes the pw
        return accountService.addNewRole(appRole);

    }

    @PostMapping(path = "/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
        accountService.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRoleName());


    }


    @GetMapping(path = "refreshToken")
    public void refreshToken(HttpServletRequest request , HttpServletResponse response) throws IOException {
        //give the refresh token in the header
        String authorizationToken = request.getHeader("Authorization");
        if (authorizationToken!=null && authorizationToken.startsWith("Bearer")){
            try{
                String refreshToken =authorizationToken.substring(7);
                Algorithm algorithm =Algorithm.HMAC256(JWTUtil.SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                AppUser appUser = accountService.loadUserByUsername(username);
                String jwtAccessToken = JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+1*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",appUser.getAppRoles().stream().map(ga ->ga.getRoleName()).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String,String> idToken = new HashMap<>();
                idToken.put("access-token",jwtAccessToken);
                idToken.put("refresh-token",refreshToken);
                response.setContentType("application/json");
                //jackson , convertir en format json
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);
            }
            catch(Exception e){
               throw e;
            }
        }
        else {
            throw new RuntimeException("Refresh token required");
        }
    }

    @GetMapping(path = "/profile")
    @PostAuthorize("hasAuthority('USER')")
    public AppUser profile(Principal principal){
        return accountService.loadUserByUsername(principal.getName());

    }
}


@Data
class RoleUserForm{
    private String username;
    private  String roleName;
}