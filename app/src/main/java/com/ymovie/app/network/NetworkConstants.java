package com.ymovie.app.network;

import com.ymovie.app.BuildConfig;

public class NetworkConstants {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL_ORIGINAL = "https://image.tmdb.org/t/p/original";
    public static final String IMAGE_BASE_URL_W500 = "https://image.tmdb.org/t/p/w500";
    public static final String AUTHENTICATION_TYPE = "Bearer ";
    public static final String TMDB_ACCESS_TOKEN_AUTH = BuildConfig.TMDB_ACCESS_TOKEN_AUTH;
}
