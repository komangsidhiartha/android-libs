package com.mamikos.mamiagent.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AreaEntity(val id: Int, val areaProvinceId: Int = 0, val areaCityId: Int = 0, val name: String = "") :  Parcelable