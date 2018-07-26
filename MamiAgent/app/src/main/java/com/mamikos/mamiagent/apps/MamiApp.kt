package com.mamikos.mamiagent.apps

import com.sidhiartha.libs.apps.BaseApplication

class MamiApp: BaseApplication()
{
    companion object {
        var sessionManager: SessionManager? = null
        var app = MamiApp()
    }

    override fun onCreate() {
        sessionManager = SessionManager(applicationContext)
        super.onCreate()
    }
}