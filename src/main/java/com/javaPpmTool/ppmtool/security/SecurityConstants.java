package com.javaPpmTool.ppmtool.security;

public class SecurityConstants {

    public static final String SIGN_UP_URLS = "/api/users/**";
    public static final String H2_URL = "h2-console/**";

    // jwt secret key is SecretKeyToGenJWTs and encrypt in sha256
    public static final String SECRET = "0c97ab3cef7c978cf81e681fd70d7a8862dea635509928d6fe68a6d0d509785a"; // "SecretKeyToGenJWTs"
    public static final String TOKEN_PREFIX = "Bearer "; // there is a space following!!!
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 3600000; // 1 hour


}
