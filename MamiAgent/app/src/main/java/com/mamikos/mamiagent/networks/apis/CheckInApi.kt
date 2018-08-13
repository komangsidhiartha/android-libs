package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod
import java.io.File

open class CheckInApi : MamikosAgentBaseApi()
{
    class FirstCheckInApi: CheckInApi()
    class UpdateCheckInApi: CheckInApi()

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod = APIMethod.UPLOAD
    override val path: String
        get() {
            return when (this)
            {
                is FirstCheckInApi -> "checkin"
                is UpdateCheckInApi -> "update/checkin"
                else -> ""
            }
        }
}