package com.mamikos.mamiagent.networks

import android.util.Base64
import android.util.Log
import com.sidhiartha.libs.BuildConfig
import com.sidhiartha.libs.apps.logIfDebug
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object GITStringBuilder
{
    private val AES_MODE_ENCRYPT = "AES/CBC/PKCS7Padding"
    private val AES_MODE_DECRYPT = "AES/CBC/NoPadding"
    private val CHARSET = "UTF-8"
    private val start = charArrayOf(98.toChar(), 48.toChar(), 51.toChar(), 102.toChar(), 52.toChar())
    private val top = charArrayOf(97.toChar(), 49.toChar(), 52.toChar(), 48.toChar(), 57.toChar())
    private val last = charArrayOf(52.toChar(), 100.toChar(), 55.toChar(), 48.toChar(), 101.toChar(), 98.toChar())
    private val ivBytes = (String(last) + String(start) + String(top)).toByteArray()

    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    private fun generateKey(password: String): SecretKeySpec {
        val key = password.toByteArray(charset("UTF-8"))
        log("SHA-256 key ", key)
        return SecretKeySpec(key, "AES")
    }

    @Throws(GeneralSecurityException::class)
    fun en(password: String, message: String?): String {
        var message = message
        if (message == null) {
            message = ""
        }
        try {
            val key = generateKey(password)

            log("message", message)

            val cipherText = encrypt(key, ivBytes, message.toByteArray(charset(CHARSET)))

            //NO_WRAP is important as was getting \n at the end
            val encoded = Base64.encodeToString(cipherText, Base64.NO_WRAP)
            log("Base64.NO_WRAP", encoded)
            return encoded
        } catch (e: UnsupportedEncodingException) {
            logIfDebug("Unsupported " + e)
            throw GeneralSecurityException(e)
        }

    }

    /**
     * More flexible AES en that doesn't encode
     *
     * @param key     AES key typically 128, 192 or 256 bit
     * @param iv      Initiation Vector
     * @param message in bytes (assumed it's already been decoded)
     * @return Encrypted cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    @Throws(GeneralSecurityException::class)
    private fun encrypt(key: SecretKeySpec, iv: ByteArray, message: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE_ENCRYPT)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        val cipherText = cipher.doFinal(message)

        log("cipherText", cipherText)

        return cipherText
    }

    /**
     * Decrypt and decode ciphertext using 256-bit AES with key generated from password
     *
     * @param password                used to generated key
     * @param base64EncodedCipherText the encrpyted message encoded with base64
     * @return message in Plain text (String UTF-8)
     * @throws GeneralSecurityException if there's an issue decrypting
     */
    @Throws(GeneralSecurityException::class)
    fun de(password: String, base64EncodedCipherText: String?): String {
        var base64EncodedCipherText = base64EncodedCipherText
        if (base64EncodedCipherText == null) {
            base64EncodedCipherText = ""
            return base64EncodedCipherText
        }
        try {
            val key = generateKey(password)

            log("base64EncodedCipherText", base64EncodedCipherText)
            val decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP)
            log("decodedCipherText", decodedCipherText)

            val decryptedBytes = decrypt(key, ivBytes, decodedCipherText)

            log("decryptedBytes", decryptedBytes)
            val message = String(decryptedBytes)
            log("message", message)


            return message
        } catch (e: UnsupportedEncodingException) {
            logIfDebug("UnsupportedEncodingException " + e)
            throw GeneralSecurityException(e)
        }

    }

    @Throws(GeneralSecurityException::class)
    fun des(password: String, base64EncodedCipherText: String?): String {
        var base64EncodedCipherText = base64EncodedCipherText
        if (base64EncodedCipherText == null) {
            base64EncodedCipherText = ""
            return base64EncodedCipherText
        }
        try {
            val key = generateKey(password)

            log("base64EncodedCipherText", base64EncodedCipherText)
            val decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP)
            log("decodedCipherText", decodedCipherText)

            val decryptedBytes = decrypt7(key, ivBytes, decodedCipherText)

            log("decryptedBytes", decryptedBytes)
            val message = String(decryptedBytes)
            log("message", message)


            return message
        } catch (e: UnsupportedEncodingException) {
            logIfDebug("UnsupportedEncodingException " + e)
            throw GeneralSecurityException(e)
        }

    }

    /**
     * More flexible AES de that doesn't encode
     *
     * @param key               AES key typically 128, 192 or 256 bit
     * @param iv                Initiation Vector
     * @param decodedCipherText in bytes (assumed it's already been decoded)
     * @return Decrypted message cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    @Throws(GeneralSecurityException::class)
    private fun decrypt(key: SecretKeySpec, iv: ByteArray,
                        decodedCipherText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE_DECRYPT)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        var decryptedBytes = cipher.doFinal(decodedCipherText)

        if (decryptedBytes.size > 0) {
            var trim = 0
            for (i in decryptedBytes.indices.reversed()) {
                if (decryptedBytes[i].toInt() == 0) {
                    trim++
                }
            }

            if (trim > 0) {
                val newArray = ByteArray(decryptedBytes.size - trim)
                System.arraycopy(decryptedBytes, 0, newArray, 0, decryptedBytes.size - trim)
                decryptedBytes = newArray
            }
        }
        log("decryptedBytes", decryptedBytes)

        return decryptedBytes
    }

    @Throws(GeneralSecurityException::class)
    private fun decrypt7(key: SecretKeySpec, iv: ByteArray,
                         decodedCipherText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE_ENCRYPT)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        var decryptedBytes = cipher.doFinal(decodedCipherText)

        if (decryptedBytes.size > 0) {
            var trim = 0
            for (i in decryptedBytes.indices.reversed()) {
                if (decryptedBytes[i].toInt() == 0) {
                    trim++
                }
            }

            if (trim > 0) {
                val newArray = ByteArray(decryptedBytes.size - trim)
                System.arraycopy(decryptedBytes, 0, newArray, 0, decryptedBytes.size - trim)
                decryptedBytes = newArray
            }
        }
        log("decryptedBytes", decryptedBytes)

        return decryptedBytes
    }

    private fun log(what: String, bytes: ByteArray) {
        logIfDebug(what + "[" + bytes.size + "] [" + bytesToHex(bytes) + "]")
    }

    private fun log(what: String, value: String) {
        if (BuildConfig.DEBUG) {
            if (value.length > 4000) {
                Log.d(what, value.substring(0, 4000))
                log(what, value.substring(4000))
            } else {
                Log.d(what, value)
            }
        }
    }

    /**
     * Converts byte array to hexidecimal useful for logging and fault finding
     *
     * @param bytes
     * @return
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        var hex = ""
        for (b in bytes) {
            val st = String.format("%02X", b)
            hex += st
        }
        return hex
    }
}
