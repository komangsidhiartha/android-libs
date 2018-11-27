package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod
import java.io.File

open class SaveKosApi : MamikosAgentBaseApi() {

    class SaveKost : SaveKosApi()

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod = APIMethod.POST
    override val path: String
        get() {
            return when (this) {
                is SaveKost -> "kos/input"
                else -> ""
            }
        }
}