package com.mamikos.mamiagent.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoomEntity(@SerializedName("_id") val _id: String,
                      @SerializedName("room-title") val roomTitle: String,
                      @SerializedName("room-type") val roomType: Int,
                      val priceTitle: String,
                      val photoUrl: PhotoUrlEntity,
                      val status: Int,
                      val availableRoom: Int,
                      val reviewCount: Int,
                      val rating: Int,
                      val price: Int,
                      var priceTitleTime: String,
                      var gender: String?,
                      var address: String,
                      var title: String?,
                      val apartmentProjectId: Int,
                      val distanceString: String,
                      val latitude: Double,
                      val longitude: Double,
                      val shareUrl: String,
                      val photoCount: Int,
                      val ownerName: String,
                      val ownerPhone: String,
                      val statuses: String,
                      val note: String?)
    : Parcelable {
    companion object {
        var STATUS_PHOTO = "photo"
        var STATUS_REVIEW = "review"
        var STATUS_SUBMIT = "submitted"
        var STATUS_CHECKIN = "checkin"
        var STATUS_VALID = "valid"
        var STATUS_INVALID = "invalid"
        var STATUS_DEFAULT = "-"
    }
}