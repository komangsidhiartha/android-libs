package com.mamikos.mamiagent

import android.app.Activity
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.networks.apis.LoginApi
import com.mamikos.mamiagent.networks.responses.StatusResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.activity_verify_phone.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject


class VerifyPhoneActivity : BaseActivity()
{
    override val layoutResource: Int = R.layout.activity_verify_phone

    override fun viewDidLoad()
    {
        btn_verify.onClick { verify() }
    }

    fun verify()
    {
        showLoadingBar()
        val api = LoginApi.VerifyPhoneApi()
        api.postParam = JSONObject().put("code", et_verify_code.text.toString()).toString()
        api.exec(StatusResponse::class.java) { response: StatusResponse?, errorMessage: String? ->
            hideLoadingBar()
            when (response)
            {
                null ->
                    errorMessage?.let { toast(it) }
                else -> {
                    logIfDebug("response " + response.toString())
                    if (response.status) {
                        MamiApp.sessionManager.isLogin = true
                        setResult(Activity.RESULT_OK)
                        this.finish()
                    }
                    else
                        response.message?.let { toast(it) }
                }
            }
        }
    }
}
