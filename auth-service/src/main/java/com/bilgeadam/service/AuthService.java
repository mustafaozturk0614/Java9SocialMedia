package com.bilgeadam.service;

import com.bilgeadam.dto.request.ActivationRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.response.LoginResponseDto;
import com.bilgeadam.excepiton.AuthManagerException;
import com.bilgeadam.excepiton.ErrorType;
import com.bilgeadam.mapper.IAuthMapper;
import com.bilgeadam.repository.IAuthRepository;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth,Long> {

    private final IAuthRepository authRepository;
    private final JwtTokenManager jwtTokenManager;
    public AuthService(IAuthRepository authRepository, JwtTokenManager jwtTokenManager) {
        super(authRepository);
        this.authRepository = authRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    public Auth register(RegisterRequestDto dto) {
        Auth auth= IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.genarateCode());
//    if (authRepository.existsByUsername(dto.getUsername())){
//        throw  new AuthManagerException(ErrorType.USERNAME_EXIST);
//    }
        try {
            return  save(auth);
        } catch (DataIntegrityViolationException e){
            throw  new AuthManagerException(ErrorType.USERNAME_EXIST);
        }catch (Exception ex){
            throw  new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }

    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> auth=authRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (auth.isEmpty()){
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
}
