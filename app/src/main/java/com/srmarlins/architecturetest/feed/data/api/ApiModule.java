package com.srmarlins.architecturetest.feed.data.api;

import android.app.Application;

import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by JaredFowler on 8/11/2016.
 */

@Module
public final class ApiModule {
    public static final HttpUrl BASE_URL = HttpUrl.parse("https://api.unsplash.com/");
    public static final int CACHE_SIZE = 52428800;

    @Provides
    @Singleton
    HttpUrl provideBaseUrl() {
        return BASE_URL;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application application) {
        File cacheDir = new File(application.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, CACHE_SIZE);
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", "Client-ID " + "f381d36df5fa45ef8790d3e20250ed290c6d1c2d340f932bbcbdc47e182da0f6")
                        .build();
                return chain.proceed(request);
            }
        };
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    Moshi provideMoshi() {
        return new Moshi.Builder().build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(HttpUrl baseUrl, OkHttpClient client, Moshi converter) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(converter))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    PhotoService providePhotoService(Retrofit retrofit) {
        return retrofit.create(PhotoService.class);
    }
}
