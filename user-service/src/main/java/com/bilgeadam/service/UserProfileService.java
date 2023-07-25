package com.bilgeadam.service;

import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.excepiton.ErrorType;
import com.bilgeadam.excepiton.UserManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.rabbitmq.producer.RegisterElasticProducer;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
/*
    findbyUsername metodu yazalım kullanıcı ismine gore bir userprofile donsun
    bu işlemi cahce kullanarak tasarlayalım


 */
@Service
public class UserProfileService  extends ServiceManager<UserProfile,String> {

    private final IUserProfileRepository userProfileRepository;
    private  final JwtTokenManager jwtTokenManager;

    private final IAuthManager authManager;
    private final CacheManager cacheManager;

    private final RegisterElasticProducer registerElasticProducer;

    public UserProfileService(IUserProfileRepository userProfileRepository, JwtTokenManager jwtTokenManager, IAuthManager authManager, CacheManager cacheManager, RegisterElasticProducer registerElasticProducer) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
        this.cacheManager = cacheManager;
        this.registerElasticProducer = registerElasticProducer;
    }

    public  Boolean createNewUser(UserSaveRequestDto dto){
        try {
            UserProfile userProfile=IUserMapper.INSTANCE.toUserProfile(dto);
              save(userProfile);

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
        cacheManager.getCache("findByStatus").evict(userProfile.get().getStatus());
        cacheManager.getCache("findByStatus2").evict(userProfile.get().getStatus());
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        cacheManager.getCache("findByStatus").evict(userProfile.get().getStatus());
        cacheManager.getCache("findByStatus2").evict(userProfile.get().getStatus());
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
            UserProfile userProfile=IUserMapper.INSTANCE.toUserProfile(registerModel);
            save(userProfile);
           registerElasticProducer.sendNewUser(IUserMapper.INSTANCE.toRegisterElasticModel(userProfile));
            return  true;
        }catch (Exception e){
            e.printStackTrace();
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
    /**
     * FındByStatus
     */
    @Cacheable(value = "findByStatus")
    public List<UserProfile> findByStatus(EStatus status){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<UserProfile> list=userProfileRepository.findByStatus(status);
       if(list.isEmpty()){
            throw new RuntimeException("Herhangi bir veri bulanumadı");
        }
        return list;
    }
    @Cacheable(value = "findByStatus2",key = "#status.toUpperCase()")
    public List<UserProfile> findByStatus(String status){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<UserProfile> list=new ArrayList<>();
        try {
            list =userProfileRepository.findByStatus(EStatus.valueOf(status.toUpperCase(Locale.ENGLISH)));
        }catch (Exception e){
            e.printStackTrace();
            throw new UserManagerException(ErrorType.STATUS_NOT_FOUND);
        }

        if(list.isEmpty()){
            throw new RuntimeException("Herhangi bir veri bulanumadı");
        }
        return list;
    }

    public Page<UserProfile> findAllByPageable(int pageSize, int pageNumber, String direction, String sortParameter) {
        Sort sort=Sort.by(Sort.Direction.fromString(direction),sortParameter);
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        return userProfileRepository.findAll(pageable);

    }

    public Slice<UserProfile> findAllBySlice(int pageSize, int pageNumber, String direction, String sortParameter) {
        Sort sort=Sort.by(Sort.Direction.fromString(direction),sortParameter);
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        return userProfileRepository.findAll(pageable);
    }

    public Optional<UserProfile> getUser(String username){
        return userProfileRepository.getUser(username);
    }

   public List<UserProfile> findAllActiveProfile(){

        return userProfileRepository.findAllActiveProfile();
    }

    public     List<UserProfile> findUserGtId(Long authId){

        return userProfileRepository.findUserGtId(authId);
    }

    public List<UserProfile> findUserGtIdAndStatus(Long authId,EStatus status){

        return userProfileRepository.findUserGtIdAndStatus(authId,status);
    }
}
