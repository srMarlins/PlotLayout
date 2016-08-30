package com.srmarlins.architecturetest.feed.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JaredFowler on 8/15/2016.
 */
public class ProfileImage implements Parcelable {
    public String small;
    public String medium;
    public String large;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.small);
        dest.writeString(this.medium);
        dest.writeString(this.large);
    }

    public ProfileImage() {
    }

    protected ProfileImage(Parcel in) {
        this.small = in.readString();
        this.medium = in.readString();
        this.large = in.readString();
    }

    public static final Parcelable.Creator<ProfileImage> CREATOR = new Parcelable.Creator<ProfileImage>() {
        @Override
        public ProfileImage createFromParcel(Parcel source) {
            return new ProfileImage(source);
        }

        @Override
        public ProfileImage[] newArray(int size) {
            return new ProfileImage[size];
        }
    };
}
