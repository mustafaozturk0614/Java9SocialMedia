package com.bilgeadam.controller;

import com.bilgeadam.dto.request.ActivationRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.response.LoginResponseDto;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.service.AuthService;
import com.bilgeadam.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping(REGISTER)
    public ResponseEntity<Auth> register(@RequestBody @Valid RegisterRequestDto dto){
        return  ResponseEntity.ok(authService.register(dto));
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
}
