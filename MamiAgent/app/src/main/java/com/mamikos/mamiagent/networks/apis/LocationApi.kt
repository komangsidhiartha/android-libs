package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

open class LocationApi : MamikosAgentBaseApi() {

    class ProvinceApi : LocationApi()
    class CityApi(val provinceId: String) : LocationApi()
    class SubDistrictApi(val cityId: String) : LocationApi()

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod
        get() = APIMethod.GET
    override val path: String
        get() {
            return when (this) {
                is ProvinceApi -> "area/province"
                is CityApi -> "area/province/$provinceId/city"
                is SubDistrictApi -> "area/city/$cityId/subdistrict"
                else -> ""
            }
        }
}