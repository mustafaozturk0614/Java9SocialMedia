package com.bilgeadam.constant;

public class EndPoints {
    public static final String VERSION="api/v1";
    public static  final String ELASTIC=VERSION+ "/elastic";
    public static  final String USER=ELASTIC+"/user";
    public static  final String FOLLOW=ELASTIC+"/follow";

    //Genel
    public static final String FINDALL="/findall";
    public static final String DELETEBYID="/deletebyid/{id}";

    public static final String SAVE="/save";
    public static final String UPDATE="/update";
    public static final String FINDBYUSERNAME="/findbyusername";

    // USER
    public static final String ACTIVATION="/activation";
    public static final String FINDBYSTATUS = "/findbystatus";


}
