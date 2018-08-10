package com.mamikos.mamiagent.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ReviewEntity
    : Parcelable {
    var id = ""
    var clean = 0
    var happy = 0
    var safe = 0
    var pricing = 0
    var roomFacilities = 0
    var publicFacilities = 0
    var content = ""
    var photo = ""

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