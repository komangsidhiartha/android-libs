package com.sidhiartha.libs

import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics



class BaseApplication: Application()
{
    private var firebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate()
    {
        super.onCreate()
        instance = this
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    override fun onTerminate()
    {
        instance = null
        super.onTerminate()
    }

    fun sendEvent(id: String, name: String)
    {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    companion object
    {
        var instance: BaseApplication? = null
    }
}