package com.mamikos.mamiagent.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PassCodeEntity(val code: String = "", val phoneNumber: String = "", val name: String = "") :  Parcelable