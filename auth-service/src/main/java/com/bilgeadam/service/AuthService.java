package com.bilgeadam.service;

import com.bilgeadam.dto.request.ActivationRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.request.UpdateRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.excepiton.AuthManagerException;
import com.bilgeadam.excepiton.ErrorType;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.IAuthMapper;
import com.bilgeadam.rabbitmq.producer.ActivationProducer;
import com.bilgeadam.rabbitmq.producer.MailProducer;
import com.bilgeadam.rabbitmq.producer.RegisterProducer;
import com.bilgeadam.repository.IAuthRepository;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth,Long> {

    private final IAuthRepository authRepository;
    private final JwtTokenManager jwtTokenManager;

    private final IUserManager userManager;

    private final RegisterProducer registerProducer;
    private final ActivationProducer activationProducer;
    private final MailProducer mailProducer;
    private final CacheManager cacheManager;

    private  final PasswordEncoder passwordEncoder;


    public AuthService(IAuthRepository authRepository, JwtTokenManager jwtTokenManager, IUserManager userManager, RegisterProducer registerProducer, ActivationProducer activationProducer, MailProducer mailProducer, CacheManager cacheManager, PasswordEncoder passwordEncoder) {
        super(authRepository);
        this.authRepository = authRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userManager = userManager;
        this.registerProducer = registerProducer;
        this.activationProducer = activationProducer;
        this.mailProducer = mailProducer;
        this.cacheManager = cacheManager;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        System.out.println(passwordEncoder.encode(dto.getPassword()));
        System.out.println(passwordEncoder.encode(dto.getPassword()));


        Auth auth= IAuthMapper.INSTANCE.toAuth(dto);

        auth.setActivationCode(CodeGenerator.genarateCode());
//    if (authRepository.existsByUsername(dto.getUsername())){
//        throw  new AuthManagerException(ErrorType.USERNAME_EXIST);
//    }
        try {
            save(auth);
        //TODO bir manager paketi içinde bir interface e metot yazılacak
            // ve yazılan metot uzerinden user servie ile iteşime geçilecek

            String token="Bearer "+jwtTokenManager.createToken(auth.getId(),auth.getRole()).get();
            userManager.createNewUser(IAuthMapper.INSTANCE.toUserSaveRequestDto(auth),token);
            return  IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        } catch (DataIntegrityViolationException e){
            throw  new AuthManagerException(ErrorType.USERNAME_EXIST);
        }catch (Exception ex){
         //   delete(auth);
            throw  new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }
    }


    public RegisterResponseDto registerWithRabbitmq(RegisterRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Auth auth= IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.genarateCode());

        try {
            save(auth);
            // rabbit mq uzrerinden veri aktarcagız
        cacheManager.getCache("findByStatus2").evict(auth.getStatus());
           cacheManager.getCache("findByStatus").evict(auth.getStatus());
            try {

            }catch (Exception e){
                e.printStackTrace();
            }

            mailProducer.sendMail(IAuthMapper.INSTANCE.toMailModel(auth));
            registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));

            return  IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        } catch (DataIntegrityViolationException e){
            throw  new AuthManagerException(ErrorType.USERNAME_EXIST);
        }catch (Exception ex){
            //   delete(auth);
            ex.printStackTrace();
            throw  new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }
    }







    public String login(LoginRequestDto dto) {

        Optional<Auth> auth=authRepository.findOptionalByUsername(dto.getUsername());
        if (auth.isEmpty()|| !passwordEncoder.matches(dto.getPassword(),auth.get().getPassword())){
            throw  new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        if(!auth.get().getStatus().equals(EStatus.ACTIVE)){
            throw  new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        Optional<String> token=jwtTokenManager.createToken(auth.get().getId(),auth.get().getRole());
        if (token.isEmpty()){
            throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
        }
//        return IAuthMapper.INSTANCE.toLoginResponseDto(auth.get());
          return  token.get();
    }
    @Transactional
    public String activateStatus(ActivationRequestDto dto) {
        Optional<Auth> auth=findById(dto.getId());
        if (auth.isEmpty()){
            throw  new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }

        if (!auth.get().getActivationCode().equals(dto.getActivationCode())){
            throw  new AuthManagerException(ErrorType.ACTIVATION_CODE_MISMATCH);
        }
      return    statusControl(auth.get());

    }

    private String statusControl(Auth auth) {
        switch (auth.getStatus()){
            case ACTIVE -> {
                return "Hesap Zaten aktif";
            }
            case PENDING,INACTIVE ->{
                auth.setStatus(EStatus.ACTIVE);
                update(auth);
              //  userManager.activateUser(auth.getId());
                activationProducer.activation(auth.getId());
                return "Hesabınız aktif edilmiştir";
            }
            case BANNED -> {
                return  "Engellenmis Hesap";
            }
            case DELETED ->{
                return  "Silinmis Hesap";
            }
            default -> {
                throw  new AuthManagerException(ErrorType.BAD_REQUEST);
            }
        }

    }

    public String deleteByAuthId(Long id) {
        Optional<Auth> auth=findById(id);
        if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (auth.get().getStatus().equals(EStatus.DELETED)){
            throw  new AuthManagerException(ErrorType.USER_NOT_FOUND,"Hesap zaten silinmiş");
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
            return  id+ " idli kullanıcı basarıyla silindi";
    }

    public String updateAuth(UpdateRequestDto dto) {
        Optional<Auth> auth=findById(dto.getId());
        if (auth.isEmpty()){
            throw  new AuthManagerException((ErrorType.USER_NOT_FOUND));
        }
        if(!auth.get().getUsername().equals(dto.getUsername()) && authRepository.existsByUsername(dto.getUsername())){
            throw new AuthManagerException(ErrorType.USERNAME_EXIST);
        }
            auth.get().setUsername(dto.getUsername());
            auth.get().setEmail(dto.getEmail());
            update(auth.get());

        return "Guncelleme Başarılı ....";
    }
}
