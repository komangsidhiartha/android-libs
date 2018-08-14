package com.mamikos.mamiagent

import android.app.Activity
import android.content.Intent
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.networks.apis.LoginApi
import com.mamikos.mamiagent.networks.responses.StatusResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.json.JSONObject

class SignInActivity : BaseActivity()
{
    override val layoutResource: Int = R.layout.activity_sign_in
    val TO_VERIFY = 1

    override fun viewDidLoad()
    {
        btn_sign_in.onClick {
            if (et_phone.text.toString().isEmpty())
                et_phone.error = "Harap isi no HP dengan benar."
            else if (MamiApp.app.isValidPhone(et_phone.text.toString()))
                signIn()
            else
                et_phone.error = "Isi no HP yang valid"
        }
    }

    fun signIn()
    {
        showLoadingBar()
        MamiApp.sessionManager.agentPhoneNumber = et_phone.text.toString()
        val api = LoginApi.ReqVerificationApi()
        api.postParam = JSONObject().put("phone", et_phone.text.toString()).toString()
        api.exec(StatusResponse::class.java)
        { response: StatusResponse?, errorMessage: String? ->
            hideLoadingBar()
            when (response)
            {
                null ->
                    errorMessage?.let { toast(it) }
                else -> {
                    if (response.status)
                        startActivityForResult<VerifyPhoneActivity>(TO_VERIFY)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TO_VERIFY && resultCode == Activity.RESULT_OK)
        {
            startActivity<ListRoomActivity>()
            this.finish()
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }
}
