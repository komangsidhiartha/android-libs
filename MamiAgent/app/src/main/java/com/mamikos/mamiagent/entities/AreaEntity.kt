package com.mamikos.mamiagent.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AreaEntity(val id: Int, val areaProvinceId: Int, val areaCityId: Int, val name: String) :  Parcelable