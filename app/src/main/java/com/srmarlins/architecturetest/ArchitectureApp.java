package com.srmarlins.architecturetest;

import android.app.Application;
import android.content.Context;

import com.srmarlins.architecturetest.feed.data.AppModule;
import com.srmarlins.architecturetest.feed.data.DaggerDataComponent;
import com.srmarlins.architecturetest.feed.data.DataComponent;
import com.srmarlins.architecturetest.feed.data.DataModule;
import com.srmarlins.architecturetest.feed.data.api.ApiModule;

/**
 * Created by JaredFowler on 8/11/2016.
 */

public class ArchitectureApp extends Application {

    private DataComponent dataComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        dataComponent = initDataComponent();
    }

    private DataComponent initDataComponent() {
        return DaggerDataComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .dataModule(new DataModule())
                .build();
    }

    public static DataComponent getDataComponent(Context context) {
        ArchitectureApp app = (ArchitectureApp) context.getApplicationContext();
        if (app.dataComponent == null) {
            app.dataComponent = app.initDataComponent();
        }
        return app.dataComponent;
    }

}
