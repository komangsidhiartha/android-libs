package com.mamikos.mamiagent.activity

import android.util.Log
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.apps.MamiApp
import com.sidhiartha.libs.activities.BaseActivity
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity() {

    override val layoutResource: Int = R.layout.activity_splash

    override fun viewDidLoad() {
        Log.e("yoho", "agent: ${MamiApp.sessionManager.agentPhoneNumber}")
        delayedProcess {
            if (MamiApp.sessionManager.agentPhoneNumber.isNotEmpty()) {
                startActivity<FormKostActivity>()
            } else {
                startActivity<PassCodeActivity>()
            }
            finish()
        }
    }
}
