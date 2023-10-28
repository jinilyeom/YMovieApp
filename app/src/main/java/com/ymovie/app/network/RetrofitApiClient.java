package com.ymovie.app.network;

import android.util.Log;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitApiClient {
    private static final String TAG = "RetrofitApiClient";

    private static Retrofit retrofit;

    private RetrofitApiClient() {
        // Private constructor
    }

    private static final Interceptor interceptor = new Interceptor() {
        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", NetworkConstants.AUTHENTICATION_TYPE + NetworkConstants.TMDB_ACCESS_TOKEN_AUTH)
                    .build();

            return chain.proceed(request);
        }
    };

    private static final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(@NonNull String s) {
            Log.d(TAG, s);
        }
    });

    public static synchronized Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(buildOkHttpClient())
                    .build();
        }

        return retrofit;
    }

    private static OkHttpClient buildOkHttpClient() {
        setHttpLoggingLevel();
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(interceptor);
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);

        return okHttpClientBuilder.build();
    }

    private static void setHttpLoggingLevel() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        httpLoggingInterceptor.setLevel(level);
    }
}
