package com.mamikos.mamiagent.activity

import com.mamikos.mamiagent.BuildConfig
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.sidhiartha.libs.activities.BaseActivity

/**
 * Created by Dedi Dot on 11/21/2018.
 * Happy Coding!
 */

class FormKostActivity : BaseActivity(){

    override val layoutResource: Int
        get() = R.layout.activity_form_kost

    override fun viewDidLoad() {
        UtilsHelper.log("ywyayayayayya "+ BuildConfig.SHOW_LOG)
        UtilsHelper.log("ywyayayayayya "+ BuildConfig.SHOW_FABRIC)
    }

}