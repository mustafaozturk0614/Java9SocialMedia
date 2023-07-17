package com.bilgeadam.dto.request;

import com.bilgeadam.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateRequestDto {

    private String token;
    @NotBlank(message = "Kullanıcı adı boş geçilemez")
    @Size(min =2 ,max=20  ,message = "Kullanıcı adı enaz 2 karakter en fazla 20 karakter olabilir" )
    private  String username;
    @Email
    private  String email;
    private String phone;
    private String avatar;
    private String address;
    private String about;

}
