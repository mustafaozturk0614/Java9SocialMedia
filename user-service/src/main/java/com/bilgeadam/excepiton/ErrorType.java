package com.bilgeadam.excepiton;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_ERROR_SERVER(5200,"Sunucu Hatası",HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4200,"Parametre Hatası",HttpStatus.BAD_REQUEST),
    USER_NOT_CREATED(4210,"Kullanıcı olusturulamadı",HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVE(4211,"Aktive Edilmemiş hesap lütfen hesabınız aktif hale getirin" ,HttpStatus.FORBIDDEN ),
    USER_NOT_FOUND(4212,"Boyle bir kullanıcı bulunamadı!" ,HttpStatus.NOT_FOUND),
    INVALID_TOKEN(4213,"Geçersiz token" ,HttpStatus.BAD_REQUEST ),
    TOKEN_NOT_CREATED(4214,"Token olusturlamadı" ,HttpStatus.BAD_REQUEST),
    STATUS_NOT_FOUND(4215,"Boyle bir kullanıcı durumu bulunamadı" ,HttpStatus.BAD_REQUEST );

    private  int code;
    private String message;
    HttpStatus httpStatus;
}
