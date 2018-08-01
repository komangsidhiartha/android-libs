package com.mamikos.mamiagent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mamikos.mamiagent.apps.MamiApp
import com.sidhiartha.libs.activities.BaseActivity
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity()
{
    override val layoutResource: Int = R.layout.activity_splash

    override fun viewDidLoad()
    {
        delayedProcess {
            if (MamiApp.sessionManager!!.isLogin)
                startActivity<MainActivity>()
            else
                startActivity<SignInActivity>()
        }
    }
}
