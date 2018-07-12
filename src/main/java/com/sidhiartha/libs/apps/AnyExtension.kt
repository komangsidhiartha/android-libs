package com.sidhiartha.libs.apps

import android.util.Log
import com.sidhiartha.libs.BuildConfig

fun Any.logIfDebug(message: String)
{
    if (BuildConfig.DEBUG) Log.w(this.javaClass.simpleName, message)
}