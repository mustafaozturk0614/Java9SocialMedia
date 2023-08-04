package com.bilgeadam.service;

import com.bilgeadam.excepiton.ElasticManagerException;
import com.bilgeadam.excepiton.ErrorType;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserProfileService  extends ServiceManager<UserProfile,String> {

    private final IUserProfileRepository userProfileRepository;

    public UserProfileService( IUserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
    }

    public Page<UserProfile> findAllByPageable(int pageSize, int pageNumber, String direction, String sortParameter) {
        Sort sort=Sort.by(Sort.Direction.fromString(direction),sortParameter);
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        return userProfileRepository.findAll(pageable);

    }

    public List<UserProfile> findAllContainingEmail(String value) {
        return userProfileRepository.findAllByEmailContainingIgnoreCase(value);
    }

    public List<UserProfile> findAllByStatus(String status) {

        return userProfileRepository.findAllByStatus(EStatus.valueOf(status.toUpperCase(Locale.ENGLISH)));
    }

    public List<UserProfile> findAllByStatusOrAddress(String status,String address) {

        return userProfileRepository.findAllByStatusOrAddress(EStatus.valueOf(status.toUpperCase(Locale.ENGLISH)),address);
    }

    public UserProfile findByUsername(String username) {

       Optional<UserProfile> userProfile= userProfileRepository.findByUsername(username);

        if (userProfile.isEmpty()){
            throw  new ElasticManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }
}
