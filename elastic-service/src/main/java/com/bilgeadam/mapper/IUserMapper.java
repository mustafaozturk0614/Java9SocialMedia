package com.bilgeadam.mapper;



import com.bilgeadam.dto.response.UserProfileResponseDto;

import com.bilgeadam.graphql.model.UserProfileInput;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {

    IUserMapper INSTANCE= Mappers.getMapper(IUserMapper.class);


    List<UserProfile> toUserProfiles(List<UserProfileResponseDto> userProfileResponseDtos);

    UserProfile toUserProfile(RegisterElasticModel model);

    UserProfile toUserProfile(UserProfileInput userProfileInput);

}
