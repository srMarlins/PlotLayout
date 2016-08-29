package com.srmarlins.architecturetest.data;

import com.srmarlins.architecturetest.data.api.ApiModule;
import com.srmarlins.architecturetest.ui.FeedActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by JaredFowler on 8/11/2016.
 */

@Singleton
@Component(modules = {AppModule.class, DataModule.class, ApiModule.class})
public interface DataComponent {
    void inject(FeedActivity feedActivity);
}
