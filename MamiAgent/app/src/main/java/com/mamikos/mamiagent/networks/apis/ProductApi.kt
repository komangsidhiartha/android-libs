package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

open class ProductApi : MamikosAgentBaseApi()
{
    class HatApi: ProductApi()
    class ShirtApi: ProductApi()
    class JeansApi: ProductApi()

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path, method)
    override val method: APIMethod
        get() = APIMethod.GET
    override val path: String
        get() {
            return when (this)
            {
                is HatApi -> "list/hat"
                is ShirtApi -> "list/shirt"
                is JeansApi -> "list/jeans"
                else -> ""
            }
        }

    override fun jsonParams(): String
    {
        return ""
    }
}