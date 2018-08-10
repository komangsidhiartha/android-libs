package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

open class LoginApi : MamikosAgentBaseApi()
{
    class ReqVerificationApi: LoginApi()
    class VerifyPhoneApi: LoginApi()

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod
        get() = APIMethod.POST
    override val path: String
        get() {
            return when (this)
            {
                is ReqVerificationApi -> "verification"
                is VerifyPhoneApi -> "login"
                else -> ""
            }
        }

    var postParam = ""

    override fun jsonParams(): String
    {
        return postParam
    }
}