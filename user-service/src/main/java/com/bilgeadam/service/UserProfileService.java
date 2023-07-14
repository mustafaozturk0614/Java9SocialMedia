package com.bilgeadam.service;

import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.excepiton.ErrorType;
import com.bilgeadam.excepiton.UserManagerException;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService  extends ServiceManager<UserProfile,Long> {

    private final IUserProfileRepository userProfileRepository;

    public UserProfileService( IUserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
    }

    public  Boolean createNewUser(UserSaveRequestDto dto){
        try {
              save(IUserMapper.INSTANCE.toUserProfile(dto));
            return true;
        }catch (Exception e){
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }

    }

    public String acivateUser(Long authId) {
        Optional<UserProfile> userProfile=userProfileRepository.findByAuthId(authId);
        if (userProfile.isEmpty()){
            throw  new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return  "Hesap başarıyla aktive edilmiştir";
    }
}
