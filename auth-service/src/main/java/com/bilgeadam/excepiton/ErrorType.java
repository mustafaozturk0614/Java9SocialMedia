package com.bilgeadam.excepiton;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_ERROR_SERVER(5100,"Sunucu Hatası",HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100,"Parametre Hatası",HttpStatus.BAD_REQUEST),
    USERNAME_EXIST(4110,"Boyle bir kullanıcı adı zaten mevuct",HttpStatus.BAD_REQUEST ),
    USER_NOT_CREATED(4111,"Kullanıcı olusturulamadı",HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4112,"Kullanıcı adı veya Şifre Hatalı",HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_ACTIVE(4113,"Aktive Edilmemiş hesap lütfen hesabınız aktif hale getirin" ,HttpStatus.FORBIDDEN ),
    USER_NOT_FOUND(4114,"Boyle bir kullanıcı bulunamadı!" ,HttpStatus.NOT_FOUND),
    ACTIVATION_CODE_MISMATCH(4115,"Hatalı aktivasyon kodu" ,HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4116,"Geçersiz token" ,HttpStatus.BAD_REQUEST ),
    TOKEN_NOT_CREATED(4117,"Token olusturlamadı" ,HttpStatus.BAD_REQUEST);
    private  int code;
    private String message;
    HttpStatus httpStatus;
}
