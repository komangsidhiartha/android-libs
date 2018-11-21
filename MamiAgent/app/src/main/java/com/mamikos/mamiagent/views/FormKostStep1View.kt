package com.mamikos.mamiagent.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class FormKostStep1View : FrameLayout {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_form_kost_step_1, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }



}