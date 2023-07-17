package com.bilgeadam.constant;

public class EndPoints {
    public static final String VERSION="api/v1";
    public static  final String AUTH=VERSION+ "/auth";

    //Genel
    public static final String FINDALL="/findall";
    public static final String DELETEBYID="/deletebyid/{id}";
    public static final String SAVE="/save";
    public static final String UPDATE="/update";

    // Auth
    public static final String REGISTER="/register";
    public static final String LOGIN="/login";
    public static final String ACTIVATION="/activation";


}
