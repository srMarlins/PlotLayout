package com.srmarlins.architecturetest.feed.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JaredFowler on 8/15/2016.
 */
public class Links implements Parcelable {
    public String self;
    public String html;
    public String photos;
    public String likes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.html);
        dest.writeString(this.photos);
        dest.writeString(this.likes);
    }

    public Links() {
    }

    protected Links(Parcel in) {
        this.self = in.readString();
        this.html = in.readString();
        this.photos = in.readString();
        this.likes = in.readString();
    }

    public static final Parcelable.Creator<Links> CREATOR = new Parcelable.Creator<Links>() {
        @Override
        public Links createFromParcel(Parcel source) {
            return new Links(source);
        }

        @Override
        public Links[] newArray(int size) {
            return new Links[size];
        }
    };
}
