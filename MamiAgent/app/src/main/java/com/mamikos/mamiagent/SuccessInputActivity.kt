package com.mamikos.mamiagent

import android.app.Activity
import android.os.Bundle
import com.sidhiartha.libs.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_success_submit.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

class SuccessInputActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_success_submit

    override fun viewDidLoad() {
        btnNextSuccess.onClick {
            startActivity<ListRoomActivity>(ListRoomActivity.SUCCESS_INPUT to true)
        }
    }
}
