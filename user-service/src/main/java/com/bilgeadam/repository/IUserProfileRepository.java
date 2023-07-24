package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserProfileRepository  extends MongoRepository<UserProfile,String> {

    Optional<UserProfile> findByAuthId(Long authId);
    Optional<UserProfile> findByUsername(String username);
    List<UserProfile> findByStatus(EStatus status);
}
