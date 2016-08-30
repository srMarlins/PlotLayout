package com.srmarlins.architecturetest.feed.data.api.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JaredFowler on 8/11/2016.
 */

public final class Photo implements Parcelable {
    public String id;
    public String created_at;
    public int width;
    public int height;
    public String color;
    public int likes;
    public User user;
    public Urls urls;
    public List<Category> categories;


    public int getColor(){
        return Integer.parseInt(color.replace("#",""), 16);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("A photo of ");
        for(int i = 0; i < categories.size(); i++){
            if(i > 1) {
                stringBuilder.append(",");
                if(categories.size() - 1 == i){
                    stringBuilder.append(" and");
                }
            }
            stringBuilder.append(" ").append(categories.get(i).title);
        }
        return stringBuilder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.created_at);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.color);
        dest.writeInt(this.likes);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.urls, flags);
        dest.writeList(this.categories);
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.id = in.readString();
        this.created_at = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.color = in.readString();
        this.likes = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.urls = in.readParcelable(Urls.class.getClassLoader());
        this.categories = new ArrayList<Category>();
        in.readList(this.categories, Category.class.getClassLoader());
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
