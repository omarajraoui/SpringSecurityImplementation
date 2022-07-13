package com.omar.jwt.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAutorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/refreshTokens")){
            //pass if youre in that end point
            //we have to do it for login too
            filterChain.doFilter(request,response);
        }
        String authorizationToken = request.getHeader("Authorization");
        if (authorizationToken!=null && authorizationToken.startsWith("Bearer")){
            try{
                String jwt =authorizationToken.substring(7);
                //begin at 7 pour enlever bearer et on doit le signer !
                Algorithm algorithm =Algorithm.HMAC256("ThisMysecret23");
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<GrantedAuthority> authorities =new ArrayList<>();
                for(String r:roles){
                    authorities.add(new SimpleGrantedAuthority(r));
                }
                UsernamePasswordAuthenticationToken authenticationToken=
                        //roles de types granted authorities so we change that
                        new UsernamePasswordAuthenticationToken(username,null,authorities);
                //authentifier cet utilisateur
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                //filterChain method (passer au suivant)
                filterChain.doFilter(request,response);



            }
            catch(Exception e){
                response.setHeader("error-message",e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            }




        }
        else {
            filterChain.doFilter(request,response);
        }


    }
}