package com.bilgeadam.controller;

import com.bilgeadam.dto.request.ActivationRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.request.UpdateRequestDto;
import com.bilgeadam.dto.response.LoginResponseDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.service.AuthService;
import com.bilgeadam.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import  static  com.bilgeadam.constant.EndPoints.*;
/*
    register --> gerekli sınıflarıda olusturalım
    login--> requestdto alsın responsedto donsun
 */
@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {

    private  final AuthService authService;
    private final JwtTokenManager jwtTokenManager;
    private final CacheManager cacheManagernager;

    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto){
        return  ResponseEntity.ok(authService.register(dto));
    }
    @PostMapping(REGISTER+"withrabbitmq")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid RegisterRequestDto dto){
        return  ResponseEntity.ok(authService.registerWithRabbitmq(dto));
    }
    @PostMapping(LOGIN)
     public  ResponseEntity<String> login(@RequestBody LoginRequestDto dto ){

        return  ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(ACTIVATION)
    public ResponseEntity<String> activateStatus(@RequestBody ActivationRequestDto dto ){

            return  ResponseEntity.ok(authService.activateStatus(dto));
    }

    @DeleteMapping(DELETEBYID)
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        return  ResponseEntity.ok(authService.deleteByAuthId(id));
    }

    @GetMapping("/gettoken")
    public  ResponseEntity<String> getToken(Long id){
        return  ResponseEntity.ok(jwtTokenManager.createToken(id).get());
    }

    @GetMapping("/getidfromtoken")
    public  ResponseEntity<Long> getIdFromToken(String token){
        return  ResponseEntity.ok(jwtTokenManager.getIdFromToken(token).get());
    }
    @GetMapping("/getrolefromtoken")
    public  ResponseEntity<String> getRoleFromToken(String token){
        return  ResponseEntity.ok(jwtTokenManager.getRoleFromToken(token).get());
    }
    @PutMapping(UPDATE)
    public  ResponseEntity<String> update(@RequestBody UpdateRequestDto dto
            ,@RequestHeader("Authorization") String token){

        return  ResponseEntity.ok(authService.updateAuth(dto));
    }

    @GetMapping(FINDALL)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')" )
    public ResponseEntity<List<Auth>> findAll(){

        return  ResponseEntity.ok(authService.findAll());
    }

   @GetMapping("/redis")
   @Cacheable(value = "redisexample")
    public  String redisExample(String value){
        try {
            Thread.sleep(2000);
            return value;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/redisdelete")
    @CacheEvict(cacheNames = "redisexample",allEntries = true)
    public void redisDelete(){
    }
    @GetMapping("/redisdelete2")
    public boolean redisDelete2(){

        try {
          //  cacheManagernager.getCache("redisexample").clear();// aynı isimle cahce lenmis butun veriyi siler
           cacheManagernager.getCache("redisexample").evict("mustafa");
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
