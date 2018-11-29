package com.mamikos.mamiagent.networks.responses

import com.google.gson.annotations.SerializedName
import com.sidhiartha.libs.networks.StatusResponse

open class MessagesResponse(error: Boolean = false, status: Boolean = false, val meta: MetaEntity? = null,
                           @SerializedName("messages") val messages: ArrayList<String>? = null) : StatusResponse(error, status)
