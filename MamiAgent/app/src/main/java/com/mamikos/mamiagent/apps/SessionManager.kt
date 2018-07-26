package com.mamikos.mamiagent.apps

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context)
{
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "mami_agent"
    private val AGENT_PHONE_NUMBER = "agent_phone_number"

    val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    val editor: SharedPreferences.Editor = pref.edit()

    var agentPhoneNumber: String
        get() = pref.getString(AGENT_PHONE_NUMBER, "")
        set(value) = editor.putString(AGENT_PHONE_NUMBER, value).apply()
}