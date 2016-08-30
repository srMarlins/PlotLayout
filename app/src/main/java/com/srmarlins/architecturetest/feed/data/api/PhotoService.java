package com.srmarlins.architecturetest.feed.data.api;

import android.support.annotation.StringDef;

import com.srmarlins.architecturetest.feed.data.api.model.Photo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JaredFowler on 8/11/2016.
 */

public interface PhotoService {
    String PHOTOS = "photos/";

    @StringDef(value={
            LATEST,
            OLDEST,
            POPULAR
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface OrderOptions {}
    String LATEST = "latest";
    String OLDEST = "oldest";
    String POPULAR = "popular";


    @GET(PHOTOS)
    Observable<Result<List<Photo>>> getPhotos(@Query("page") int pageNumber,
                                              @Query("per_page") int numItemsPerPage,
                                              @Query("order_by") @OrderOptions String orderType);


}
