package com.mamikos.mamiagent.entities

import com.google.gson.annotations.SerializedName

abstract class BasePhotoEntity(
        /*Start input new ads photo*/
       /* var main: List<Int>? = null,*/ var bedroom: List<Int>? = null, var bath: ArrayList<Int>? = null,
        var other: List<PhotoOtherEntity>? = null, @SerializedName("360") var photo360: Int = 0,
        /*Start edit ads photo*/
        var bangunan: List<PhotoOtherEntity>? = null,
        var kamar: List<PhotoOtherEntity>? = null, @SerializedName("kamar-mandi") var kamarMandi: List<PhotoOtherEntity>? = null,
        var lainnya: List<PhotoOtherEntity>? = null) {
    constructor() : this(/*null,*/ null, null, null, 0, null, null, null, null)
}