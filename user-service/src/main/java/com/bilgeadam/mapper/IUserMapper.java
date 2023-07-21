package com.bilgeadam.mapper;


import com.bilgeadam.dto.request.UpdateRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {

    IUserMapper INSTANCE= Mappers.getMapper(IUserMapper.class);


    UserProfile toUserProfile(final UserSaveRequestDto dto);


    UserProfile toUserProfile(UserProfileUpdateRequestDto dto);
    UserProfile toUserProfile(RegisterModel model);
    @Mapping(source = "authId" ,target = "id")
    UpdateRequestDto toUpdateRequestDto(final UserProfile userProfile);

    @Mapping(source = "id",target = "userProfileId")
    UserProfileResponseDto toUserProfileResponseDto(UserProfile userProfile);

}
