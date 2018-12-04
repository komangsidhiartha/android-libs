package com.mamikos.mamiagent.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.helpers.UtilsHelper
import kotlinx.android.synthetic.main.activity_list_data_form.*

/**
 * Created by Dedi Dot on 12/4/2018.
 * Happy Coding!
 */


class ListDataFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_data_form)

        UtilsHelper.setupToolbar(this, dataFormToolbar, getString(R.string.msg_exit), "", R.drawable.ic_arrow_back_white_24dp, Runnable {
            onBackPressed()
        })


    }

}