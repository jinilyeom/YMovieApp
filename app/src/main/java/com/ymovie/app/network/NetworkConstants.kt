package com.ymovie.app.network

import com.ymovie.app.BuildConfig

object NetworkConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL_ORIGINAL = "https://image.tmdb.org/t/p/original"
    const val IMAGE_BASE_URL_W500 = "https://image.tmdb.org/t/p/w500"
    const val AUTHENTICATION_TYPE = "Bearer "
    const val TMDB_ACCESS_TOKEN_AUTH = BuildConfig.TMDB_ACCESS_TOKEN_AUTH
}