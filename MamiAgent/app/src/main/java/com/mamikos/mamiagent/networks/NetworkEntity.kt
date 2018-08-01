package com.mamikos.mamiagent.networks

import com.mamikos.mamiagent.interfaces.Constants
import java.security.GeneralSecurityException


class NetworkEntity {
    private val mediaId = Constants.netEnc + String(
            charArrayOf(50.toChar(), 77.toChar(), 51.toChar(), 57.toChar(), 110.toChar(), 81.toChar(), 65.toChar(), 111.toChar(), 48.toChar(), 85.toChar(), 85.toChar(), 48.toChar(), 43.toChar(), 120.toChar(), 121.toChar(), 79.toChar(), 119.toChar(), 120.toChar())) + Constants.MURL +
            String(charArrayOf(122.toChar(), 85.toChar(), 57.toChar(), 85.toChar(), 116.toChar(), 66.toChar(), 99.toChar(), 43.toChar(), 115.toChar(), 53.toChar(), 78.toChar()))
    private val photoId = String(charArrayOf(113.toChar(), 50.toChar(), 80.toChar(), 101.toChar(), 52.toChar(), 102.toChar(), 48.toChar(), 57.toChar(), 109.toChar(), 102.toChar(), 103.toChar(), 106.toChar(), 104.toChar(), 111.toChar(), 119.toChar(), 83.toChar())) + Constants.MD

    private val photoUrl: String
        get() {
            var decode = ""
            try {
                decode = GITStringBuilder.des(photoId, mediaId)
            } catch (e: GeneralSecurityException) {
                e.printStackTrace()
            }

            return decode
        }

    fun stringUrl(): String {
        return photoUrl
    }
}