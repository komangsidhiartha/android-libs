package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

open class RoomApi : MamikosAgentBaseApi()
{
    class DataRoomApi: RoomApi()

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path, method)
    override val method: APIMethod
        get() = APIMethod.POST
    override val path: String
        get() {
            return when (this)
            {
                is RoomApi -> "stories/list" + queryParam?.let { "?" + extract(it) }
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