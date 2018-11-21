package com.mamikos.mamiagent.activity

import com.mamikos.mamiagent.R
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
                startActivity<ListRoomActivity>()
            else
                startActivity<SignInActivity>()
        }
    }
}
