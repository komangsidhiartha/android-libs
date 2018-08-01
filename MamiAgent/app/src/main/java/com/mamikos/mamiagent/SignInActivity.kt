package com.mamikos.mamiagent

import android.telephony.PhoneNumberUtils
import android.text.TextWatcher
import com.mamikos.mamiagent.apps.MamiApp
import com.sidhiartha.libs.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

class SignInActivity : BaseActivity()
{
    override val layoutResource: Int = R.layout.activity_sign_in

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

    }
}
