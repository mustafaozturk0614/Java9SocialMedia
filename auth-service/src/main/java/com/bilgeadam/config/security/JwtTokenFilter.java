package com.bilgeadam.config.security;

import com.bilgeadam.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtTokenManager jwtTokenManager;
    @Autowired
    private JwtUserDetails jwtUserDetails;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader=request.getHeader("Authorization");
        System.out.println(authorizationHeader);

        if (authorizationHeader!=null&&authorizationHeader.startsWith("Bearer ")
        ){
            String token=authorizationHeader.substring(7);
            Optional<Long> id=jwtTokenManager.getIdFromToken(token);
            UserDetails userDetails=null;
                if (id.isPresent()){
                  userDetails=   jwtUserDetails.loadUserById(id.get());
                    UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            System.out.println("===>"+userDetails);
        }


        filterChain.doFilter(request,response);
    }
}
