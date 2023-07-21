package com.bilgeadam.utility;

import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllData {

    private final UserProfileService userProfileService;

    private final IUserManager userManager;
    @PostConstruct
    public void initData(){
    List<UserProfileResponseDto>list=userManager.findAllForElasticService().getBody();
        userProfileService.saveAll(IUserMapper.INSTANCE.toUserProfiles(list));
    }

}
