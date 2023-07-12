package com.bilgeadam.controller;

import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.response.LoginResponseDto;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(REGISTER)
    public ResponseEntity<Auth> register(@RequestBody @Valid RegisterRequestDto dto){
        return  ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(LOGIN)
     public  ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto ){

        return  ResponseEntity.ok(authService.login(dto));
    }

}
