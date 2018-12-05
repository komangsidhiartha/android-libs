package com.mamikos.mamiagent.networks.apis

import com.mamikos.mamiagent.networks.MamikosAgentBaseApi
import com.sidhiartha.libs.networks.APIMethod

open class TelegramApi : MamikosAgentBaseApi() {

    class SendReport(val msgError: String) : TelegramApi()

    override val basePath: String
        get() = "https://api.telegram.org"

    override val headers: Map<String, String>?
        get() = generateAuthHeader(path)
    override val method: APIMethod
        get() = APIMethod.GET
    override val path: String
        get() {
            return when (this) {
                is SendReport -> "bot628258528:AAGUGMXePEYNfStcKZ3pkNnDWsjmGRW1scE/sendMessage?chat_id=-1001387489031&text=$msgError"
                else -> ""
            }
        }
}