package com.bilgeadam.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Security;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthServiceSecurityConfig {

    @Bean
    JwtTokenFilter getJwtTokenFilter(){
        return  new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private static final String[]  WHITELIST ={
            "/swagger-ui/**","/v3/api-docs/**","/api/v1/auth/login",
            "/api/v1/auth/registerwithrabbitmq","/api/v1/auth/register","/api/v1/auth/activation"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable();

      httpSecurity.authorizeRequests()
              .antMatchers(WHITELIST)
              .permitAll()
              .anyRequest().authenticated();
   httpSecurity.sessionManagement()
           .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        httpSecurity.authorizeRequests().antMatchers("/api/v1/auth/findall").authenticated()
//                .anyRequest().permitAll();

        httpSecurity.addFilterBefore(getJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return  httpSecurity.build();

    }
}
