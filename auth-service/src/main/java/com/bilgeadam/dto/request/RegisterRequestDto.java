package com.bilgeadam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    @NotBlank(message = "Kullanıcı adı boş geçilemez")
    @Size(min =2 ,max=20  ,message = "Kullanıcı adı enaz 2 karakter en fazla 20 karakter olabilir" )
    private  String username;
    @Email
    private  String email;
    @NotBlank(message = "Şifre boş geçilemez")
    @Size(min = 5 ,max=32, message = "Sifre  enaz 5 karakter en fazla 32 karakter olabilir")
   // @Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")
    private  String password;


}
