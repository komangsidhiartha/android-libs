package com.mamikos.mamiagent.networks.responses

import com.google.gson.annotations.SerializedName
import com.mamikos.mamiagent.entities.RoomEntity

class ListRoomResponse(error: Boolean, status: Boolean, meta: MetaEntity, message: String,
                       val data: DataListRoomResponse) : StatusResponse(error, status, meta, message)

class DataListRoomResponse(val page: Int,
                           @SerializedName("next-page") val nextPage: Int,
                           val limit: Int,
                           val offset: Int,
                           @SerializedName("has-more") val hasMore: Boolean,
                           val total: Int,
                           @SerializedName("total-str") val totalStr: String,
                           val rooms: ArrayList<RoomEntity>)