package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import jdk.jshell.Snippet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

public interface IUserProfileRepository extends ElasticsearchRepository<UserProfile,String> {
    Optional<UserProfile> findByUsername(String username);
    List<UserProfile> findAllByEmailContainingIgnoreCase(String value);
    List<UserProfile> findAllByStatus(EStatus status);
    List<UserProfile> findAllByStatusOrAddress(EStatus status,String address);

}
