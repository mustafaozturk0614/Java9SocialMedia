package com.bilgeadam.utility;

import java.util.UUID;

public class CodeGenerator {



    public static  String genarateCode(){
        String code= UUID.randomUUID().toString();
        System.out.println(code);
        String [] data=code.split("-");
        StringBuilder newCode=new StringBuilder();
        for (String string:data){
            newCode.append(string.charAt(0));
        }
        return newCode.toString();
    }

}
