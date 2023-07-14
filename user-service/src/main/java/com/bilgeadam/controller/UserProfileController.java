package com.bilgeadam.controller;

import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import  static  com.bilgeadam.constant.EndPoints.*;

/*
    update metodu olusturacagız login olduktan sonra gelen veri
    ile databaseden bir user profile bulup onu guncelleyeceğiz
    eğer username veya email den biri  değişmiş ise auth servicedede ilişkili
    auth 'u guncellesin
    eger değişmemis ise auth service e istek atmaya gerek yoktur sadece user profile
    da guncelleme yapsın

 */
@RequiredArgsConstructor
@RestController
@RequestMapping(USER)
public class UserProfileController {


    private final UserProfileService userProfileService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> createNewUser(@RequestBody UserSaveRequestDto dto){

        return  ResponseEntity.ok(userProfileService.createNewUser(dto));
    }

   @PostMapping(ACTIVATION+"/{authId}")
    public  ResponseEntity<String> activateUser(@PathVariable Long authId){
        return  ResponseEntity.ok(userProfileService.acivateUser(authId));
   }
    @PostMapping(ACTIVATION)
    public  ResponseEntity<String> activateUser2(@RequestParam Long authId){
        return  ResponseEntity.ok(userProfileService.acivateUser(authId));
    }
}
