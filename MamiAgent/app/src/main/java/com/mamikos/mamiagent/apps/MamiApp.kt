package com.mamikos.mamiagent.apps

import android.accounts.AccountManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.crashlytics.android.Crashlytics
import com.mamikos.mamiagent.interfaces.Constants
import com.sidhiartha.libs.apps.BaseApplication
import io.fabric.sdk.android.Fabric
import java.util.*

class MamiApp: BaseApplication()
{
    companion object {
        lateinit var sessionManager: SessionManager
        var app = MamiApp()
    }

    override fun onCreate() {
        sessionManager = SessionManager(applicationContext)
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Crashlytics.setUserIdentifier(MamiApp.sessionManager.agentPhoneNumber)
    }

    fun getToken(): String {
        if (sessionManager!!.token.isEmpty()) {
            return Constants.GUEST_TOKEN
        } else {
            return sessionManager!!.token
        }
    }

    fun getDevicePlatformVersionCode(): Int {
        return Build.VERSION.SDK_INT
    }

    fun getUUID(): String {
        var mUUID = ""
        val iid: String
        if (sessionManager!!.uuid.isEmpty()) {

            //TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            iid = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
            val tmDevice = "" + iid// get device_id
            mUUID = UUID.randomUUID().toString() + tmDevice
            sessionManager!!.uuid = mUUID
        } else {
            mUUID = sessionManager!!.uuid
        }
        return mUUID
    }

    fun getAndroidId(): String {
        return Settings.Secure.getString(applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID)
    }

    fun getModel(): String {
        return Build.MANUFACTURER + " " + Build.MODEL
    }

    fun getDeviceEmail(): String {
        if (sessionManager!!.email.isEmpty()) {
            val emailPattern = Patterns.EMAIL_ADDRESS
            val accounts = AccountManager.get(applicationContext).accounts
            for (account in accounts) {
                if (emailPattern.matcher(account.name).matches() && account.name.contains("gmail")) {
                    sessionManager!!.email = account.name
                }
            }
        }
        return sessionManager!!.email
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }
}