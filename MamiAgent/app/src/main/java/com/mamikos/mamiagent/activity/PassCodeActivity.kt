package com.mamikos.mamiagent.activity

import `in`.arjsna.passcodeview.PassCodeView
import android.content.Intent
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.mamikos.mamiagent.networks.apis.PassCodeApi
import com.mamikos.mamiagent.networks.responses.PassCodeResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.utils.GSONManager
import kotlinx.android.synthetic.main.activity_pass_code.*
import org.jetbrains.anko.toast
import org.json.JSONObject

/**
 * Created by Dedi Dot on 11/27/2018.
 * Happy Coding!
 */

class PassCodeActivity : BaseActivity() {

    private var da: PassCodeView? = null

    override val layoutResource: Int = R.layout.activity_pass_code

    override fun viewDidLoad() {
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

    }

    private fun goAuth(it: String) {
        loading?.show()
        val api = PassCodeApi.CodeApi()
        val jsonData = JSONObject()
        jsonData.put("code", it)
        api.postParam = jsonData.toString()
        api.exec(PassCodeResponse::class.java) { response: PassCodeResponse?, errorMessage: String? ->
            when (response) {
                null -> errorMessage?.let {
                    toast(it)
                }
                else -> {
                    UtilsHelper.log("sip ${GSONManager.toJson(response)}")
                    if (response.status) {
                        MamiApp.sessionManager.agentPhoneNumber = response.data.phoneNumber
                        val intent = Intent(this, FormKostActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        toast(response.message + "")
                        da?.reset()
                    }
                }
            }
            loading?.hide()
        }
    }
}