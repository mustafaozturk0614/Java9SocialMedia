package com.bilgeadam.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileInput {
    private String username;
    private String email;
    @UniqueElements
    private Long authId;
}
