package com.bilgeadam.controller;

import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @PutMapping(UPDATE)
    public ResponseEntity<String> update(@RequestBody @Valid UserProfileUpdateRequestDto dto){
        return  ResponseEntity.ok(userProfileService.updateUserProfile(dto));
    }


    @GetMapping(FINDALL)
    public ResponseEntity<List<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @PostMapping(FINDBYUSERNAME)                    //PathVariable
    public ResponseEntity<UserProfile> findByUsername(@RequestParam String username){
        return ResponseEntity.ok(userProfileService.findByUsername(username));
    }
    @GetMapping(FINDBYSTATUS)                    //PathVariable
    public ResponseEntity<List<UserProfile>> findByStatus(@RequestParam EStatus status){
        return ResponseEntity.ok(userProfileService.findByStatus(status));
    }
    @GetMapping(FINDBYSTATUS+"2")                    //PathVariable
    public ResponseEntity<List<UserProfile>> findByStatus(@RequestParam String status){
        return ResponseEntity.ok(userProfileService.findByStatus(status));
    }

    @GetMapping(FINDALL+"forelastic")
    public ResponseEntity<List<UserProfileResponseDto>> findAllForElasticService(){
  List<UserProfileResponseDto>  list=userProfileService.findAll().stream()
                .map(x-> IUserMapper.INSTANCE.toUserProfileResponseDto(x)).collect(Collectors.toList());

  return ResponseEntity.ok(list);
    }

    @GetMapping("/findallbypageable")
    public ResponseEntity<Page<UserProfile>> findAllByPageable(int pageSize,int pageNumber,@RequestParam(required = false,defaultValue = "ASC") String direction,@RequestParam(required = false,defaultValue = "id") String sortParameter){

        return  ResponseEntity.ok(userProfileService.findAllByPageable(pageSize,pageNumber,direction,sortParameter));
    }

    @GetMapping("/findallbyslice")
    public ResponseEntity<Slice<UserProfile>> findAllBySlice(int pageSize, int pageNumber, @RequestParam(required = false,defaultValue = "ASC") String direction, @RequestParam(required = false,defaultValue = "id") String sortParameter){

        return  ResponseEntity.ok(userProfileService.findAllBySlice(pageSize,pageNumber,direction,sortParameter));
    }

    @GetMapping ("/getuser")
    public  ResponseEntity<Optional<UserProfile>> getUser(String username){

        return  ResponseEntity.ok(userProfileService.getUser(username));
    }

    @GetMapping ("/finallactiveprofile")
    public  ResponseEntity<List<UserProfile>> findAllActiveProfile(){

        return  ResponseEntity.ok(userProfileService.findAllActiveProfile());
    }

    @GetMapping ("/findUserGtId")
    public  ResponseEntity<List<UserProfile>> findUserGtId(Long authId){

        return  ResponseEntity.ok(userProfileService.findUserGtId(authId));
    }
    @GetMapping("/findUserGtIdAndStatus")
    public ResponseEntity< List<UserProfile>> findUserGtIdAndStatus(Long authId,EStatus status){

        return ResponseEntity.ok(userProfileService.findUserGtIdAndStatus(authId,status));
    }
}
