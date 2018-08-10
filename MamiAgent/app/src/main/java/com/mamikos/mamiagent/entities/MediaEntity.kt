package com.mamikos.mamiagent.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaEntity(var id: Int, var photoId: Int?, var photo: String, var type: String?) : Parcelable