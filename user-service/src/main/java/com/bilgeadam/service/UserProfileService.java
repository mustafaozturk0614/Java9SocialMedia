package com.bilgeadam.service;

import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.excepiton.ErrorType;
import com.bilgeadam.excepiton.UserManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
/*
    findbyUsername metodu yazalım kullanıcı ismine gore bir userprofile donsun
    bu işlemi cahce kullanarak tasarlayalım


 */
@Service
public class UserProfileService  extends ServiceManager<UserProfile,Long> {

    private final IUserProfileRepository userProfileRepository;
    private  final JwtTokenManager jwtTokenManager;

    private final IAuthManager authManager;

    public UserProfileService(IUserProfileRepository userProfileRepository, JwtTokenManager jwtTokenManager, IAuthManager authManager) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
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

     @Transactional
    public String updateUserProfile(UserProfileUpdateRequestDto dto) {
        Optional<Long> authId=jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw  new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile=userProfileRepository.findByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw  new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (!userProfile.get().getEmail().equals(dto.getEmail())||
                !userProfile.get().getUsername().equals(dto.getUsername())){
            userProfile.get().setEmail(dto.getEmail());
            userProfile.get().setUsername(dto.getUsername());
            // auth-microserivceine ulaştıgımız istek attığımız kısım
            authManager.update(IUserMapper.INSTANCE.toUpdateRequestDto(userProfile.get()));
        }

        userProfile.get().setAvatar(dto.getAvatar());
        userProfile.get().setAbout(dto.getAbout());
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAddress(dto.getAddress());
            update(userProfile.get());
            return "Guncelleme başarılı";

    }

    public Boolean createNewUserWithRabbitmq(RegisterModel registerModel) {
        try {
            save(IUserMapper.INSTANCE.toUserProfile(registerModel));
            return  true;
        }catch (Exception e){
            throw  new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    @Cacheable(value = "findByUsername",key = "#username.toLowerCase()")
    public UserProfile findByUsername(String username) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<UserProfile> userProfile=userProfileRepository.findByUsername(username.toLowerCase());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }
}
