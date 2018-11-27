package com.mamikos.mamiagent.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 8/7/17.
 */

@SuppressWarnings("WeakerAccess")
public class PhotoOtherEntity implements Parcelable {
    private int id;

    private String description;
    private String type;
    private String fileName;

    private PhotoUrlEntity photoUrl;
    private PhotoUrlEntity url;

    public PhotoOtherEntity() {

    }

    public String getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public PhotoUrlEntity getPhotoUrl() {
        return photoUrl;
    }

    public PhotoUrlEntity getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.description);
    }

    public PhotoOtherEntity(Parcel in) {
        this.id = in.readInt();
        this.description = in.readString();
    }

    public static final Creator<PhotoOtherEntity> CREATOR = new Creator<PhotoOtherEntity>() {
        @Override
        public PhotoOtherEntity createFromParcel(Parcel source) {
            return new PhotoOtherEntity(source);
        }

        @Override
        public PhotoOtherEntity[] newArray(int size) {
            return new PhotoOtherEntity[size];
        }
    };
}
