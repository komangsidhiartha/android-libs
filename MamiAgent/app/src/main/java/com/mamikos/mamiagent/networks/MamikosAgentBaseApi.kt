package com.mamikos.mamiagent.networks

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpUpload
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
import java.io.File
import java.security.GeneralSecurityException
import java.util.HashMap
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

abstract class MamikosAgentBaseApi : BaseAPI()
{
    override val basePath: String = "http://songturu.mamikos.com/api/v1/giant/"
    var context: Context? = null
    var formData: List<Pair<String, Any?>>? = null
    var fileUpload: File? = null

    fun generateAuthHeader(url: String): Map<String, String> {
        logIfDebug("url before:\n $url")
        var url = url.replace(basePath + "/", "")

        logIfDebug("url in between:\n $url")

        if (url.indexOf("?") > 0) {
            url = url.substring(0, url.indexOf("?"))
        }

        logIfDebug("url after:\n $url")

        val timeStamp = System.currentTimeMillis() / 1000

        val header = HashMap<String, String>()
        if (method == APIMethod.UPLOAD)
        {
            header["Content-Type"] = "multipart/form-data; boundary="+ System.currentTimeMillis()
        }
        else {
            header["Content-Type"] = "application/json"
        }
        header["X-GIT-Time"] = "" + timeStamp
        header["Authorization"] = "GIT devel:" + MamiApp.sessionManager.agentPhoneNumber

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
                /* var decode = ""
                try {
                    decode = GITStringBuilder.de(NetworkEntity().stringUrl(), json!!.obj().get("data").toString())
                } catch (e: GeneralSecurityException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                } */

                handler(GSONManager.fromJson(json!!.obj(), kelas), null)
            }
        }

        when (method)
        {
            APIMethod.GET -> get(localHandler)
            APIMethod.DELETE -> delete(localHandler)
            APIMethod.POST -> post(localHandler)
            APIMethod.PUT -> put(localHandler)
            APIMethod.UPLOAD -> upload(localHandler)
            else -> Log.i("TAG", "unsupported $method method")
        }
    }

    fun upload(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath$path".httpUpload(Method.POST, formData).header(headers).
                dataParts { request, url -> listOf(
                        DataPart(fileUpload!!, "file", "image/jpeg"))
                }
                .responseJson(handler)
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