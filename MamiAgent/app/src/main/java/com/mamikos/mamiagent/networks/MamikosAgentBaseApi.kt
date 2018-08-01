package com.mamikos.mamiagent.networks

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.apps.SessionManager
import com.mamikos.mamiagent.interfaces.Constants
import com.sidhiartha.libs.apps.logIfDebug
import com.sidhiartha.libs.networks.APIMethod
import com.sidhiartha.libs.networks.BaseAPI
import com.sidhiartha.libs.utils.GSONManager
import org.apache.commons.codec.binary.Hex
import org.json.JSONException
import java.security.GeneralSecurityException
import java.util.HashMap
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

abstract class MamikosAgentBaseApi : BaseAPI()
{
    override val basePath: String = "http://kay.mamikos.com/api/v2/"
    var context: Context? = null

//    fun init(context: Context)
//    {
//        this.context = context
//    }

    fun generateAuthHeader(url: String, method: APIMethod): Map<String, String> {
        logIfDebug("url before:\n $url")
        var url = url.replace(basePath + "/", "")

        logIfDebug("url in between:\n $url")

        if (url.indexOf("?") > 0) {
            url = url.substring(0, url.indexOf("?"))
        }

        logIfDebug("url after:\n $url")

        val timeStamp = System.currentTimeMillis() / 1000

        var auth = ""
        var data = ""

        when (method) {
            APIMethod.POST -> data += "POST "

            APIMethod.PUT -> data += "PUT "

            APIMethod.GET -> data += "GET "

            APIMethod.DELETE -> data += "DELETE "
            else -> {
            }
        }

        data += url + " " + timeStamp

        Log.d("data", data)
        Log.d("token", MamiApp.app.getToken())
//                context?.let { SessionManager(it).agentPhoneNumber })
//        SessionManager(context).agentPhoneNumber)

        try {
            auth = ("GIT "
                    + encodeHeader(Constants.CLIENT_API_KEY, data) + ":" + MamiApp.app.getToken())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        header["X-GIT-Time"] = "" + timeStamp
        header["Authorization"] = auth

        return header
    }

    fun <T> exec(kelas: Class<T>, handler: (response: T?, errorMessage: String?) -> Unit)
    {
        val localHandler = { request: Request, response: Response, result: Result<Json, FuelError> ->
            val (json, error) = result

            Log.w("Network Manager", "request $request")
            Log.w("Network Manager", "response $response")
            Log.w("Network Manager", "json $json")
            Log.w("Network Manager", "error $error")
            if (error != null)
            {
                handler(null, error.localizedMessage)
            } else
            {
                var decode = ""
                try {
                    decode = GITStringBuilder.de(NetworkEntity().stringUrl(), json!!.obj().get("data").toString())
                } catch (e: GeneralSecurityException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                handler(GSONManager.fromJson(decode, kelas), null)
            }
        }

        when (method)
        {
            APIMethod.GET -> get(localHandler)
            APIMethod.DELETE -> delete(localHandler)
            APIMethod.POST -> post(localHandler)
            APIMethod.PUT -> put(localHandler)
            else -> Log.i("TAG", "unsupported $method method")
        }
    }

    @Throws(Exception::class)
    fun encodeHeader(key: String, data: String): String {
        val sha256HMAC = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(key.toByteArray(),
                "HmacSHA256")
        sha256HMAC.init(secretKey)

        return String(Hex.encodeHex(sha256HMAC.doFinal(data.toByteArray())))
    }
}