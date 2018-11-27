package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

open class PassCodeApi : MamikosAgentBaseApi() {

    class CodeApi : PassCodeApi()

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod
        get() = APIMethod.POST
    override val path: String
        get() {
            return when (this) {
                is CodeApi -> "authentication"
                else -> ""
            }
        }
}