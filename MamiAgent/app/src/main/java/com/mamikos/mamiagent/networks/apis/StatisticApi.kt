package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.BuildConfig
import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

class StatisticApi(val fromDate: String?, val toDate: String?) : MamikosAgentBaseApi() {

    override val basePath: String = BuildConfig.BASE_URL_OLD

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod = APIMethod.GET
    override val path: String
        get() {
            if (fromDate.isNullOrEmpty() || toDate.isNullOrEmpty()) return "statistics"
            else return "statistics?from_date=$fromDate&to_date=$toDate"
        }
}