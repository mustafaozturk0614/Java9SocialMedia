package com.bilgeadam.utility;

import org.springframework.stereotype.Service;

@Service
public class JwtTokenManager {
//    @Value("${jwt.secretKey}")
//    String secretKey;
//    @Value("${jwt.issuer}")
//    String issuer;
//    @Value("${jwt.audience}")
//    String audience;
//
//
//
//    public Optional<String> createToken(Long id){
//        String token=null;
//        Date date=new Date(System.currentTimeMillis()+(1000*60*5));
//        try {
//            token= JWT.create()
//                    .withAudience(audience)
//                    .withIssuer(issuer)
//                    .withClaim("id",id)
//                    .withIssuedAt(new Date())
//                    .withExpiresAt(date)
//                    .sign(Algorithm.HMAC512(secretKey));
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        return  Optional.ofNullable(token);
//    }
//
//    public Optional<Long> getIdFromToken(String token ){
//        try{
//            Algorithm algorithm=Algorithm.HMAC512(secretKey);
//            JWTVerifier verifier= JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
//            DecodedJWT decodedJWT=verifier.verify(token);
//            if (decodedJWT==null){
//                throw  new UserManagerException(ErrorType.INVALID_TOKEN);
//            }
//            Long id=decodedJWT.getClaim("id").asLong();
//            return  Optional.of(id);
//        }catch (Exception e){
//            System.out.println(e.toString());
//            throw  new UserManagerException(ErrorType.INVALID_TOKEN);
//        }
//    }
//
//    public Optional<String> getRoleFromToken(String token ){
//        try{
//            Algorithm algorithm=Algorithm.HMAC512(secretKey);
//            JWTVerifier verifier= JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
//            DecodedJWT decodedJWT=verifier.verify(token);
//            if (decodedJWT==null){
//                throw  new UserManagerException(ErrorType.INVALID_TOKEN);
//            }
//            String role=decodedJWT.getClaim("role").asString();
//            return  Optional.of(role);
//        }catch (Exception e){
//            System.out.println(e.toString());
//            throw  new UserManagerException(ErrorType.INVALID_TOKEN);
//        }
//    }


}
