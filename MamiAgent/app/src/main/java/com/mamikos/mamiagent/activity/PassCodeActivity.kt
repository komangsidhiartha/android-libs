package com.mamikos.mamiagent.activity

import `in`.arjsna.passcodeview.PassCodeView
import android.content.Intent
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.mamikos.mamiagent.networks.apis.PassCodeApi
import com.mamikos.mamiagent.networks.apis.TelegramApi
import com.mamikos.mamiagent.networks.responses.MessagesResponse
import com.mamikos.mamiagent.networks.responses.PassCodeResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.utils.GSONManager
import kotlinx.android.synthetic.main.activity_pass_code.*
import org.json.JSONObject
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.mamikos.mamiagent.BuildConfig
import com.mamikos.mamiagent.networks.GITStringBuilder
import com.mamikos.mamiagent.networks.NetworkEntity
import org.jetbrains.anko.toast


/**
 * Created by Dedi Dot on 11/27/2018.
 * Happy Coding!
 */

class PassCodeActivity : BaseActivity() {

    private var da: PassCodeView? = null

    override val layoutResource: Int = R.layout.activity_pass_code

    override fun viewDidLoad() {

        AndroidNetworking.initialize(this)

        da = PassCodeView(this)
        da?.digitLength = 6
        da?.setTypeFace(UtilsHelper.getFontRegular(this))
        da?.setOnTextChangeListener {
            UtilsHelper.log(it)
            if (it.length == 6) {
                goAuth(it)
            }
        }
        da?.setEmptyDrawable(R.drawable.bullet_border_green)
        da?.setFilledDrawable(R.drawable.bullet_full_green)
        passCodeLinearLayout.addView(da)

        if (!UtilsHelper.isNetworkConnected(this)) {
            UtilsHelper.showDialogYes(this, "", "Koneksi tidak stabil", Runnable {}, 0)
        }

    }

    private fun goAuth(it: String) {
        loading?.show()
        val api = PassCodeApi.CodeApi()
        val jsonData = JSONObject()
        jsonData.put("code", it.toInt())
        api.postParam = jsonData.toString()
        api.exec(PassCodeResponse::class.java) { response: PassCodeResponse?, errorMessage: String? ->
            when (response) {
                null -> errorMessage?.let {
                    sendReport("$it paijomestiX")
                    sendAgain(it)
                }
                else -> {
                    UtilsHelper.log("sip ${GSONManager.toJson(response)}")
                    if (response.status) {
                        MamiApp.sessionManager.agentPhoneNumber = response.data.phoneNumber
                        val intent = Intent(this, FormKostActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        toast(response.message.toString())
                        da?.reset()
                    }
                    loading?.hide()
                }
            }
        }
    }

    private fun sendAgain(it: String) {
        AndroidNetworking.post(BuildConfig.BASE_URL + "/authentication")
                .addHeaders("Authorization", "GIT devel:087839439584")
                .addHeaders("X-GIT-Time", "${System.currentTimeMillis() / 1000}")
                .addHeaders("Content-Type", "application/json").addBodyParameter("code", it)
                .setPriority(Priority.HIGH).build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val decode = GITStringBuilder.de(NetworkEntity().stringUrl(), response?.get("data").toString())
                        val json = JSONObject(decode)
                        if (json.optJSONObject("data") != null) {
                            MamiApp.sessionManager.agentPhoneNumber = json.optJSONObject("data")
                                    .optString("phone_number")
                            val intent = Intent(this@PassCodeActivity, FormKostActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            toast(json.optString("message"))
                            da?.reset()
                        }
                        UtilsHelper.log(decode)
                        loading?.hide()
                    }

                    override fun onError(error: ANError) {
                        sendReport("${error.message.toString()} paijomestiHAHAHA1")
                        sendReport("${error.errorBody} paijomestiHAHAHA2")
                        sendReport("${error.errorDetail} paijomestiHAHAHA3")
                        loading?.hide()
                    }
                })
    }

    private fun sendReport(s: String) {
        val reportApi = TelegramApi.SendReport(s)
        reportApi.exec(MessagesResponse::class.java) { _: MessagesResponse?, _: String? -> }
    }

}