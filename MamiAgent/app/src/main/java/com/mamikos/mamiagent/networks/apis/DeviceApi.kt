package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

open class DeviceApi : MamikosAgentBaseApi()
{
    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod
        get() = APIMethod.POST
    override val path: String
        get() = "stories/list" + queryParam?.let { "?" + extract(it) }

    var queryParam: List<Pair<String, String>>? = null
    var postParam = ""

    override fun jsonParams(): String
    {
        return postParam
    }
}