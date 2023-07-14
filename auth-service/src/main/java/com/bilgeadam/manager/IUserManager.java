package com.bilgeadam.manager;

import com.bilgeadam.dto.request.UserSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.bilgeadam.constant.EndPoints.ACTIVATION;
import static com.bilgeadam.constant.EndPoints.SAVE;


@FeignClient(url = "http://localhost:7072/api/v1/user",decode404 = true,name = "auth-userprofile")
public interface IUserManager {

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> createNewUser(@RequestBody UserSaveRequestDto dto);

    @PostMapping(ACTIVATION+"/{authId}")
    public  ResponseEntity<String> activateUser(@PathVariable Long authId);

    @PostMapping(ACTIVATION)
    public  ResponseEntity<String> activateUser2(@RequestParam Long authId);
}
