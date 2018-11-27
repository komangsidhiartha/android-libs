package com.mamikos.mamiagent.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by git-dev on 7/3/17.
 *
 */

@Parcelize data class PhotoFormEntity(var cover: Int? = null, @SerializedName("main") var mains: Int? = null) : BasePhotoEntity(), Parcelable
