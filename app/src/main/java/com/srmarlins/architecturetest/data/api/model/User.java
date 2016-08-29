package com.srmarlins.architecturetest.data.api.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JaredFowler on 8/15/2016.
 */
public class User implements Parcelable {
    public String id;
    public String username;
    public String name;
    public ProfileImage profile_image;
    public Links links;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.name);
        dest.writeParcelable(this.profile_image, flags);
        dest.writeParcelable(this.links, flags);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.name = in.readString();
        this.profile_image = in.readParcelable(ProfileImage.class.getClassLoader());
        this.links = in.readParcelable(Links.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
