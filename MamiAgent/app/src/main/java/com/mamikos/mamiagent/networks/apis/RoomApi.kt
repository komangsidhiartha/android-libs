package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

open class RoomApi : MamikosAgentBaseApi()
{
    class DataRoomApi: RoomApi()
    class ListDataRoomApi: RoomApi()
    class ListEditedRoomApi: RoomApi()
    class ReviewRoomApi: RoomApi()
    class UpdateReviewRoomApi(val id: String): RoomApi()
    class SaveDataRoomApi: RoomApi()

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod
        get() {
            return when (this)
            {
                is DataRoomApi -> APIMethod.GET
                is ListDataRoomApi -> APIMethod.GET
                is ListEditedRoomApi -> APIMethod.GET
                is ReviewRoomApi -> APIMethod.UPLOAD
                is UpdateReviewRoomApi -> APIMethod.UPLOAD
                is SaveDataRoomApi -> APIMethod.POST
                else -> APIMethod.POST
            }
        }
    override val path: String
        get() {
            return when (this)
            {
                is DataRoomApi -> "stories/list" + queryParam?.let { "?" + extract(it) }
                is ListDataRoomApi -> "list" + queryParam?.let { "?" + extract(it) }
                is ListEditedRoomApi -> "edited" + queryParam?.let { "?" + extract(it) }
                is ReviewRoomApi -> "review"
                is UpdateReviewRoomApi -> "update/review/$id"
                is SaveDataRoomApi -> "data"
                else -> ""
            }
        }

    var queryParam: List<Pair<String, String>>? = null
    var postParam = ""

    override fun jsonParams(): String
    {
        return postParam
    }
}