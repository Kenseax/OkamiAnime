package ru.anime.okami.utils;

public class HeaderConst {

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN_VALUE = "http://localhost:3000";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_METHODS_VALUE = "POST, PUT, PATCH, GET, DELETE, OPTIONS";

    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS_VALUE = "Origin, X-Api-Key, X-Requested-With, Content-Type, Accept, Authorization";

    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE = "true";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS_VALUE = "Set-Cookie";

    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CACHE_CONTROL_VALUE = "max-age=604800";
}
