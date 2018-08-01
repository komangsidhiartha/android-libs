package com.mamikos.mamiagent.entities

import android.os.Parcel
import android.os.Parcelable


data class PhotoUrlEntity(val real: String, val medium: String, val small: String, val large: String): Parcelable
{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(real)
        parcel.writeString(medium)
        parcel.writeString(small)
        parcel.writeString(large)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoUrlEntity> {
        override fun createFromParcel(parcel: Parcel): PhotoUrlEntity {
            return PhotoUrlEntity(parcel)
        }

        override fun newArray(size: Int): Array<PhotoUrlEntity?> {
            return arrayOfNulls(size)
        }
    }
}
