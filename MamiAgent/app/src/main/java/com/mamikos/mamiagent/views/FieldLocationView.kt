package com.mamikos.mamiagent.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.helpers.UtilsHelper
import kotlinx.android.synthetic.main.view_field_location.view.*
import android.support.v4.content.ContextCompat

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class FieldLocationView : FrameLayout {

    var data: PopupWindow? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_field_location, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setOnClickListener {
            val v = FormKostStep1View(context)
            data = PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true) // Creation of popup
            data?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent)) // biar bisa dismiss outside
            data?.isOutsideTouchable = false
            UtilsHelper.log("ahahahahaha")
            data?.showAsDropDown(it)
        }
    }

    fun setHint(strHint: String) {
        locationHintTextView.text = strHint
    }

    fun setName(strName: String) {
        locationNameTextView.text = strName
    }


}