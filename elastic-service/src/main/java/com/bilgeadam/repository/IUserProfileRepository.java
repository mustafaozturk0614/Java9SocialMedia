package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.UserProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IUserProfileRepository extends ElasticsearchRepository<UserProfile,String> {
}
