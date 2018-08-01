package com.mamikos.mamiagent.entities

import com.google.gson.annotations.SerializedName

data class RoomEntity(@SerializedName("room-title") val roomTitle: String,
                      @SerializedName("room-type") val roomType: Int,
                      @SerializedName("price-title") val priceTitle: String,
                      @SerializedName("_id") val _id: String,
                      @SerializedName("status-title") val statusTitle: String,
                      val photoUrl: PhotoUrlEntity,
                      val status: Int,
                      val availableRoom: Int,
                      val reviewCount: Int,
                      val rating: Int,
                      val price: Int,
                      var priceTitleTime: String,
                      var gender: String,
                      var address: String,
                      var title: String)
{

}