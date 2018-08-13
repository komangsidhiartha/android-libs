package com.mamikos.mamiagent.entities

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewEntity(
    var id: Int,
    var clean: Int,
    var happy: Int,
    var safe: Int,
    var pricing: Int,
    var roomFacilities: Int,
    var publicFacilities: Int,
    var content: String,
    var photo: String): Parcelable
{
    constructor() : this(0, 0, 0, 0, 0, 0, 0, "", "")

    fun toListPair(): List<Pair<String, Any>>
    {
        return listOf(
                Pair("id", id),
                Pair("clean", clean),
                Pair("happy",happy ),
                Pair("safe", safe),
                Pair("pricing", pricing),
                Pair("room_facilities", roomFacilities),
                Pair("public_facilities", publicFacilities),
                Pair("content", content),
                Pair("photo", photo))
    }
}