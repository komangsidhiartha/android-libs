package com.sidhiartha.libs.networks

import android.text.TextUtils
import android.util.Log
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.result.Result
import com.sidhiartha.libs.apps.logIfDebug
import com.sidhiartha.libs.utils.GSONManager
import java.io.File
import java.util.ArrayList

/**
 * Created by sidhiartha on 21/01/18.
 */

enum class APIMethod
{
    GET, DELETE, POST, PUT, HEAD, PATCH, UPLOAD
}

abstract class BaseAPI
{

    abstract val headers: Map<String, String>?

    abstract val basePath: String

    abstract val method: APIMethod

    abstract val path: String

    abstract val params : String

    open var formData: List<Pair<String, Any?>>? = null

    open val filesToUpload: ArrayList<DataPart> = arrayListOf()

    protected fun extract(queryParams: List<Pair<String, String>>): String
    {
        var result = ""
        queryParams.forEach {
            if (!TextUtils.isEmpty(result))
                result += "&"
            result += "${it.first}=${it.second}"
        }
        return result
    }

    fun <T> execute(kelas: Class<T>, handler: (response: T?, errorMessage: String?) -> Unit)
    {
        val localHandler = { request: Request, response: Response, result: Result<Json, FuelError> ->
            val (json, error) = result

            Log.w("Network Manager", "request $request")
            Log.w("Network Manager", "response $response")
            Log.w("Network Manager", "json $json")
            Log.w("Network Manager", "error $error")
            handleResponse(json, error, kelas, handler)
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

    open fun <T> handleResponse(json: Json?, error: FuelError?, kelas: Class<T>, handler: (response: T?, errorMessage: String?) -> Unit)
    {
        if (error != null)
            handler(null, error.localizedMessage)
        else
            handler(GSONManager.fromJson(json!!.obj(), kelas), null)
    }

    fun get(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath/$path".httpGet().header(generateHeader()).responseJson(handler)
    }

    fun delete(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath/$path".httpDelete().body(params).header(generateHeader()).responseJson(handler)
    }

    fun post(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath/$path".httpPost().body(params).header(generateHeader()).responseJson(handler)
    }

    fun put(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath/$path".httpPut().body(params).header(generateHeader()).responseJson(handler)
    }

    fun upload(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath/$path".httpUpload(Method.POST, formData).header(generateHeader()).
                dataParts { _, _ -> filesToUpload }
                .responseJson(handler)
    }

    private fun generateHeader(): Map<String, String>
    {
        when (method)
        {
            APIMethod.GET -> return appendHeaderWithAcceptJson()
            APIMethod.DELETE -> return appendHeaderWithJsonSpecific()
            APIMethod.POST -> return appendHeaderWithJsonSpecific()
            APIMethod.PUT -> return appendHeaderWithJsonSpecific()
            APIMethod.UPLOAD -> return appendHeaderWithMultipartFormType()
            else -> return mapOf()
        }
    }

    private fun appendHeaderWithJsonSpecific(): Map<String, String>
    {
        val newHeader = appendHeaderWithJsonContentType()
        newHeader.plus(appendHeaderWithAcceptJson())
        return newHeader
    }

    private fun appendHeaderWithJsonContentType(): Map<String, String>
    {
        val jsonSpecificMap = mapOf("Content-Type" to "application/json")
        return headers?.plus(jsonSpecificMap) ?: jsonSpecificMap
    }

    private fun appendHeaderWithAcceptJson(): Map<String, String>
    {
        val acceptJsonMap = mapOf("Accept" to "application/json")
        return headers?.plus(acceptJsonMap) ?: acceptJsonMap
    }

    private fun appendHeaderWithMultipartFormType(): Map<String, String>
    {
        val multipartFormSpecificMap = mapOf("Content-Type" to "multipart/form-data; boundary=${System.currentTimeMillis()}")
        return headers?.plus(multipartFormSpecificMap) ?: multipartFormSpecificMap
    }
}