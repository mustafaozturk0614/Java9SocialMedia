package com.bilgeadam.service;

import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

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
}
