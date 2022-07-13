package com.omar.jwt;

public class JWTUtil {
    public static final String SECRET="ThisMysecret23";
    public static final String AUTH_HEADER="Authorization";
    //2 min
    public static final long EXPIRE_ACCESS_TOKEN=2*60*1000;
    public static final long EXPIRE_REFRESH_TOKEN=20*60*1000;

}
