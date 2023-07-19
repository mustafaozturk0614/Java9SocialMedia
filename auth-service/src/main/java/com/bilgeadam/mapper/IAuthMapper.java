package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.dto.response.LoginResponseDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.rabbitmq.model.MailModel;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {

    IAuthMapper INSTANCE= Mappers.getMapper(IAuthMapper.class);

    Auth toAuth(final RegisterRequestDto dto);

    LoginResponseDto toLoginResponseDto(final Auth auth);

    @Mapping(source = "id",target = "authId")
    UserSaveRequestDto toUserSaveRequestDto(final Auth auth);

    RegisterResponseDto toRegisterResponseDto(Auth auth);

    @Mapping(source = "id" ,target = "authId")
    RegisterModel  toRegisterModel(Auth auth);

    MailModel toMailModel(Auth auth);

}
