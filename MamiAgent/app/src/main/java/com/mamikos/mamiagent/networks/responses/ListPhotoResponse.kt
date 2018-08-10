package com.mamikos.mamiagent.networks.responses

import com.google.gson.annotations.SerializedName
import com.mamikos.mamiagent.entities.MediaEntity

class ListPhotoResponse(error: Boolean, status: Boolean, meta: MetaEntity, message: String,
                        var data: PhotoDataResponse) : StatusResponse(error, status, meta, message)

class PhotoDataResponse(var cover: ArrayList<MediaEntity>?,
                        var bangunan: ArrayList<MediaEntity>?,
                        var kamar: ArrayList<MediaEntity>?,
                        @SerializedName("kamar-mandi") var kamarMandi: ArrayList<MediaEntity>?,
                        var lainnya: ArrayList<MediaEntity>?)