package com.sidhiartha.libs.networks

import android.text.TextUtils
import android.util.Log
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import com.sidhiartha.libs.utils.GSONManager

/**
 * Created by sidhiartha on 21/01/18.
 */

enum class APIMethod
{
    GET, DELETE, POST, PUT, HEAD, PATCH
}

abstract class BaseAPI
{

    abstract val headers: Map<String, String>?

    abstract val basePath: String

    abstract val method: APIMethod

    abstract val params: List<Pair<String, String>>

    abstract val path: String

//    protected open fun jsonParams() : String {
//        return ""
//    }

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
            if (error != null)
            {
                handler(null, error.localizedMessage)
            } else
            {
                handler(GSONManager.fromJson(json!!.obj(), kelas), null)
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

//    fun <T> executeJson(kelas: Class<T>, handler: (response: T?, errorMessage: String?) -> Unit) {
//        val localHandler = { request: Request, response: Response, result: Result<Json, FuelError> ->
//            val (json, error) = result
//
//            Log.w("Network Manager", "request $request")
//            Log.w("Network Manager", "response $response")
//            Log.w("Network Manager", "json $json")
//            Log.w("Network Manager", "error $error")
//            if (error != null) {
//                handler(null, error.localizedMessage)
//            } else {
//                handler(GSONManager.fromJson(json!!.obj(), kelas), null)
//            }
//        }
//
//        when (method) {
//            APIMethod.GET -> get(localHandler)
//            APIMethod.DELETE -> delete(localHandler)
//            APIMethod.POST -> postJson(localHandler)
//            APIMethod.PUT -> put(localHandler)
//            else -> Log.i("TAG", "unsupported $method method")
//        }
//    }

    private fun get(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath$path".httpGet().header(headers).responseJson(handler)
    }

    private fun delete(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath$path".httpDelete(params).header(headers).responseJson(handler)
    }

//    private fun postJson(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit) {
//        "$basePath$path".httpPost().body(jsonParams()).header(headers?.plus(mapOf("Content-Type" to "application/json"))).responseJson(handler)
//    }

    private fun post(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath$path".httpPost(params).header(headers).responseJson(handler)
    }

    private fun put(handler: (request: Request, response: Response, result: Result<Json, FuelError>) -> Unit)
    {
        "$basePath$path".httpPut(params).header(headers).responseJson(handler)
    }
}