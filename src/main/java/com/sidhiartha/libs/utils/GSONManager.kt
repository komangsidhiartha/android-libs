package com.sidhiartha.libs.utils

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import org.json.JSONObject
import java.util.*

/**
 * Created by sidhiartha on 23/01/18.
 */
class GSONManager
{
    companion object
    {
        val gson: Gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date::class.java, DateTypeAdapter()).create()

        fun <T> fromJson(json: String, kelas: Class<T>): T
        {
            return gson.fromJson(json, kelas)
        }

        fun <T> fromJson(json: JSONObject, kelas: Class<T>): T
        {
            return fromJson(json.toString(), kelas)
        }
    }
}