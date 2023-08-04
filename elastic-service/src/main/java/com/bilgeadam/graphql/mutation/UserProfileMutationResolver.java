package com.bilgeadam.graphql.mutation;

import com.bilgeadam.excepiton.ElasticManagerException;
import com.bilgeadam.excepiton.ErrorType;
import com.bilgeadam.graphql.model.UserProfileInput;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.service.UserProfileService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMutationResolver  implements GraphQLMutationResolver {

    private final UserProfileService userProfileService;


    public Boolean createUserProfile(UserProfileInput userProfileInput){
        try {
            userProfileService.save(IUserMapper.INSTANCE.toUserProfile(userProfileInput));
            return  true;
        }catch (Exception e){
         throw    new ElasticManagerException(ErrorType.USER_NOT_CREATED);
        }

    }
}
