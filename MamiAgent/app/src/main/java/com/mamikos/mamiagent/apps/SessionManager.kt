package com.mamikos.mamiagent.apps

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context)
{
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "mami_agent"
    private val UUID = "uuid"
    private val EMAIL = "email"
    private val TOKEN = "user_token"
    private val USER_ID = "user_id"
    private val IS_LOGIN = "is_login"
    private val AGENT_PHONE_NUMBER = "agent_phone_number"
    private val PATH_CAMERA = "path_camera"

    val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    val editor: SharedPreferences.Editor = pref.edit()

    var uuid: String
        get() = pref.getString(UUID, "")
        set(value) = editor.putString(UUID, value).apply()

    var email: String
        get() = pref.getString(EMAIL, "")
        set(value) = editor.putString(EMAIL, value).apply()

    var token: String
        get() = pref.getString(TOKEN, "c98c1915a6b8f203083f6afbf34ef8b60ff431a11ee1c8a3a52918f4d11cc7d8")
        set(value) = editor.putString(TOKEN, value).apply()

    var userId: String
        get() = pref.getString(USER_ID, "")
        set(value) = editor.putString(USER_ID, value).apply()

    var isLogin: Boolean
        get() = pref.getBoolean(IS_LOGIN, false)
        set(value) = editor.putBoolean(IS_LOGIN, value).apply()

    var agentPhoneNumber: String
        get() = pref.getString(AGENT_PHONE_NUMBER, "087839439584")
        set(value) = editor.putString(AGENT_PHONE_NUMBER, value).apply()

    var pathCamera: String
        get() = pref.getString(PATH_CAMERA, "")
        set(value) = editor.putString(PATH_CAMERA, value).apply()
}