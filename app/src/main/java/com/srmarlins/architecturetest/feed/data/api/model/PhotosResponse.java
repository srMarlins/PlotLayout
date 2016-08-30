package com.srmarlins.architecturetest.feed.data.api.model;

import java.util.List;

/**
 * Created by JaredFowler on 8/11/2016.
 */

public final class PhotosResponse {
    public final List<Photo> photos;

    public PhotosResponse(List<Photo> photos) {
        this.photos = photos;
    }
}
