package com.mamikos.mamiagent.activity

import com.mamikos.mamiagent.R
import com.sidhiartha.libs.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_success_submit.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.singleTop

class SuccessInputActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_success_submit

    override fun viewDidLoad() {
        btnNextSuccess.onClick {
            startActivity(intentFor<ListRoomActivity>(ListRoomActivity.SUCCESS_INPUT to true).singleTop())
        }
    }

    override fun onBackPressed() {

    }
}
