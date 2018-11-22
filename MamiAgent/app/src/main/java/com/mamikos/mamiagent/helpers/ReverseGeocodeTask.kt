package com.mamikos.mamiagent.helpers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.StrictMode
import android.text.TextUtils
import com.google.android.gms.maps.model.LatLng
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * Created by Dedi Dot on 11/22/2018.
 * Happy Coding!
 */

class ReverseGeocodeTask(private var context: Context) {

    var addressText = ""
    var fullAddress = ""
    var streetName = ""
    var subDistrict = ""
    var city = ""
    var listFullAddress: List<Address> = arrayListOf()
    private lateinit var callback: GeoCodeTaskCallBack

    fun run(ltLng: LatLng) {
        ReverseGeocodeTaskX().execute(ltLng)
    }

    fun setCallBack(cb: GeoCodeTaskCallBack) {
        callback = cb
    }

    private inner class ReverseGeocodeTaskX : AsyncTask<LatLng, Void, String>() {

        override fun doInBackground(vararg params: LatLng): String? {

            val latLng = params[0]
            try {
                /*Sometimes getFromLocation Error Timeout server response*/
                val locale = Locale("id")
                val addressGeocoder = Geocoder(context, locale)

                if (isCancelled) {
                    return null
                }

                if (latLng != null) {
                    listFullAddress = addressGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                    if (listFullAddress != null && listFullAddress!!.size > 0) {
                        val address = listFullAddress!![0]
                        streetName = address.thoroughfare
                        convertAddress(address)
                    }
                }
            } catch (e: IOException) {
                /*Alternative from timeout server response*/
                try {
                    val listFullAddress = getStringFromLocation(latLng.latitude, latLng.longitude)
                    if (fullAddress != null && listFullAddress!!.size > 0) {
                        val address = listFullAddress[0]
                        convertAddress(address)
                    }
                } catch (e1: IOException) {
                    e1.printStackTrace()
                } catch (e1: JSONException) {
                    e1.printStackTrace()
                }

            }

            if (streetName == null) addressText = fullAddress
            else addressText = streetName + ", " + fullAddress
            return addressText
        }


        override fun onPostExecute(addressText: String) {
            //query.id(R.id.tv_address_autocomplete_value).text(addressText)
            UtilsHelper.log("complete address ${addressText}")
            callback.getFullAddress(addressText)
        }


    }

    private fun convertAddress(address: Address) {
        if (address.locality != null && address.locality != "" && !TextUtils.isEmpty(address.locality)) {
            if (!fullAddress?.isEmpty()!!) fullAddress = ""

            /*Kecamatan*/
            if (fullAddress == "" || TextUtils.isEmpty(fullAddress)) fullAddress = address.locality
            else fullAddress = fullAddress + ", " + address.locality
            subDistrict = address.locality
        }
        if (address.subAdminArea != null && address.subAdminArea != "" && !TextUtils.isEmpty(address.subAdminArea)) {
            /*Kota*/
            if (fullAddress == "" || TextUtils.isEmpty(fullAddress)) fullAddress = address.subAdminArea
            else fullAddress = fullAddress + ", " + address.subAdminArea
            city = address.subAdminArea
        }
        if (address.adminArea != null && address.adminArea != "" && !TextUtils.isEmpty(address.adminArea)) {
            if (fullAddress == "" || TextUtils.isEmpty(fullAddress)) fullAddress = address.adminArea
            else fullAddress = fullAddress + ", " + address.adminArea
        }

        /*Analytics address from alternatives plan*/
        if (fullAddress.length == 0 || fullAddress == "") {
            if (address.getAddressLine(0) != null) {
                fullAddress = ellipsize(address.getAddressLine(0), 60)
            }
        }

        UtilsHelper.log("complete address ${addressText}")
        UtilsHelper.log("complete address ${fullAddress}")

    }

    fun ellipsize(input: String, maxLength: Int): String {
        return if (input == null || input.length <= maxLength) {
            input
        } else input.substring(0, maxLength - 3) + "..."
    }

    @Throws(IOException::class, JSONException::class)
    fun getStringFromLocation(lat: Double, lng: Double): List<Address> {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val address = String.format(Locale.ENGLISH, "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1\$f,%2\$f&sensor=true&language=" + Locale.getDefault().country, lat, lng)

        val url: URL
        val reader: BufferedReader
        val stringBuilder: StringBuilder = StringBuilder()

        url = URL(address)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.readTimeout = 15 * 1000
        connection.connect()

        reader = BufferedReader(InputStreamReader(connection.inputStream))

        var b = reader.read()
        while ((b) != -1) {
            stringBuilder.append(b.toChar())
            b = reader.read()
        }

        val retList = ArrayList<Address>()
        val jsonObject = JSONObject(stringBuilder.toString())

        if ("OK".equals(jsonObject.getString("status"), ignoreCase = true)) {
            val results = jsonObject.getJSONArray("results")
            for (i in 0 until results.length()) {
                val result = results.getJSONObject(i)
                val indiStr = result.getString("formatted_address")
                val addr = Address(Locale.getDefault())
                addr.setAddressLine(0, indiStr)
                retList.add(addr)
            }
        }
        return retList
    }


    interface GeoCodeTaskCallBack {
        fun getFullAddress(address: String)
    }
}