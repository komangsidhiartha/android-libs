package com.mamikos.mamiagent.helpers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.StrictMode
import android.text.TextUtils
import com.google.android.gms.maps.model.LatLng
import com.mamikos.mamiagent.views.CustomLoadingView
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
    private var loading: CustomLoadingView? = null

    fun run(ltLng: LatLng) {
        if (loading == null) {
            loading = CustomLoadingView(context)
        }
        loading?.show()
        ReverseGeocodeTaskX().execute(ltLng)
    }

    fun setCallBack(cb: GeoCodeTaskCallBack) {
        callback = cb
    }

    private inner class ReverseGeocodeTaskX : AsyncTask<LatLng, Void, String>() {

        private lateinit var latLng: LatLng

        override fun doInBackground(vararg params: LatLng): String? {

            latLng = params[0]

            try {
                /*Sometimes getFromLocation Error Timeout server response*/
                val locale = Locale("id")
                val addressGeocoder = Geocoder(context, locale)

                if (isCancelled) {
                    return null
                }

                UtilsHelper.log("oke 11x ${latLng.latitude} ${latLng.longitude}")

                if (latLng != null) {
                    listFullAddress = addressGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 2)

                    if (listFullAddress != null && listFullAddress!!.size >= 2) {
                        val address = listFullAddress!![1]
                        UtilsHelper.log("oke 1")
                        if (address != null && address.hasLatitude() && address.hasLongitude()) {
                            streetName = address.thoroughfare
                            convertAddress(address)
                            UtilsHelper.log("oke 2")
                        } else {
                            UtilsHelper.log("oke 3")
                            option2(latLng)
                        }
                    } else {
                        UtilsHelper.log("oke 4")
                    }
                } else {
                    UtilsHelper.log("oke 5")
                }
            } catch (e: Exception) {
                UtilsHelper.log("oke 6")
                option2(latLng)
            }

            if (streetName == null) addressText = fullAddress
            else addressText = streetName + ", " + fullAddress
            return addressText
        }


        override fun onPostExecute(addressText: String) {
            //query.id(R.id.tv_address_autocomplete_value).text(addressText)
            UtilsHelper.log("complete address ${addressText}")
            callback.getFullAddress(addressText, latLng)
            loading?.hide()
        }

    }

    private fun option2(latLng: LatLng) {
        /*Alternative from timeout server response*/
        try {
            UtilsHelper.log("oke 11 ${latLng.latitude} ${latLng.longitude}")
            val listFullAddress = getStringFromLocation(latLng.latitude, latLng.longitude)
            UtilsHelper.log("oke 8 ${listFullAddress.size}")
            if (fullAddress != null && listFullAddress!!.size >= 2) {
                val address = listFullAddress[1]
                UtilsHelper.log("oke 9")
                convertAddress(address)
            } else {
                UtilsHelper.log("oke 10")
            }
        } catch (e1: Exception) {
            UtilsHelper.log("oke 7")
            e1.printStackTrace()
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
                fullAddress = address.getAddressLine(0)
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

        UtilsHelper.log("from location 1")

        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        UtilsHelper.log("from location 2")

        val address = String.format(Locale.ENGLISH, "https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat,$lng&radius=1000000&sensor=true&key=AIzaSyAANygKT5R_5LiPkUxBzRPcBWI4k4yRQiU")

        val url: URL
        val reader: BufferedReader
        val stringBuilder = StringBuilder()

        url = URL(address)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.readTimeout = 15 * 1000
        connection.connect()

        reader = BufferedReader(InputStreamReader(connection.inputStream))
        UtilsHelper.log("from location 3")
        var b = reader.read()
        while ((b) != -1) {
            stringBuilder.append(b.toChar())
            b = reader.read()
        }

        val retList = ArrayList<Address>()
        val jsonObject = JSONObject(stringBuilder.toString())
        UtilsHelper.log("from location 4 "+jsonObject)
        if ("OK".equals(jsonObject.getString("status"), ignoreCase = true)) {
            UtilsHelper.log("from location 6")
            val results = jsonObject.getJSONArray("results")
            for (i in 0 until results.length()) {
                val result = results.getJSONObject(i)
                val indiStr = result.getString("formatted_address")
                val addr = Address(Locale.getDefault())
                addr.setAddressLine(0, indiStr)
                retList.add(addr)
            }
        }
        UtilsHelper.log("from location 5")
        return retList
    }


    interface GeoCodeTaskCallBack {
        fun getFullAddress(address: String, latLng: LatLng)
    }
}