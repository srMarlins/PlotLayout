package com.srmarlins.architecturetest.data;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import com.srmarlins.architecturetest.data.api.PhotoService;
import com.srmarlins.architecturetest.data.api.model.Photo;
import com.srmarlins.architecturetest.data.api.model.PhotosResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.Result;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JaredFowler on 8/11/2016.
 */

public final class PhotoManager {

    public static final int NUM_ITEMS_PER_PAGE = 50;

    private PhotoService photoService;

    @Inject
    public PhotoManager(PhotoService photoService) {
        this.photoService = photoService;
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public Subscription getPhotos(@NonNull final Subscriber<List<Photo>> subscriber, @Nullable int pageNumber, @Nullable @PhotoService.OrderOptions String orderBy) {
        return photoService.getPhotos(pageNumber, NUM_ITEMS_PER_PAGE, orderBy == null ? PhotoService.POPULAR : orderBy)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<List<Photo>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Result<List<Photo>> photoList) {
                        subscriber.onNext(photoList.response().body());
                    }
                });
    }
}
