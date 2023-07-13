package com.bilgeadam.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilgeadam.excepiton.AuthManagerException;
import com.bilgeadam.excepiton.ErrorType;
import com.bilgeadam.repository.enums.ERole;
import com.fasterxml.jackson.databind.DatabindException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenManager {
    @Value("${jwt.secretKey}")
    String secretKey;
    @Value("${jwt.issuer}")
    String issuer;
    @Value("${jwt.audience}")
    String audience;


    public Optional<String> createToken(Long id){
        String token=null;
        Date date=new Date(System.currentTimeMillis()+(1000*60*5));
        try {
            token= JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("id",id)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC512(secretKey));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return  Optional.ofNullable(token);
    }
    public Optional<String> createToken(Long id, ERole role){
        String token=null;
        Date date=new Date(System.currentTimeMillis()+(1000*60*5));
        try {
            token= JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("id",id)
                    .withClaim("role",role.toString())
//                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC512(secretKey));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return  Optional.ofNullable(token);
    }
    public Optional<Long> getIdFromToken(String token ){
        try{
            Algorithm algorithm=Algorithm.HMAC512(secretKey);
            JWTVerifier verifier= JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT=verifier.verify(token);
            if (decodedJWT==null){
                throw  new AuthManagerException(ErrorType.INVALID_TOKEN);
            }
            Long id=decodedJWT.getClaim("id").asLong();
            return  Optional.of(id);
        }catch (Exception e){
            System.out.println(e.toString());
            throw  new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
    }


    public Optional<String> getRoleFromToken(String token ){
        try{
            Algorithm algorithm=Algorithm.HMAC512(secretKey);
            JWTVerifier verifier= JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT=verifier.verify(token);
            if (decodedJWT==null){
                throw  new AuthManagerException(ErrorType.INVALID_TOKEN);
            }
            String role=decodedJWT.getClaim("role").asString();
            return  Optional.of(role);
        }catch (Exception e){
            System.out.println(e.toString());
            throw  new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
    }


}
