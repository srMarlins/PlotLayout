package com.srmarlins.architecturetest.data;

import android.app.Application;
import android.content.SharedPreferences;

import com.srmarlins.architecturetest.data.api.ApiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by JaredFowler on 8/11/2016.
 */

@Module(
        includes = ApiModule.class
)
public class DataModule {
    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences("architecturetest", MODE_PRIVATE);
    }
}
