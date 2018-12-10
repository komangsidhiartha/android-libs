package com.mamikos.mamiagent.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import kotlinx.android.synthetic.main.view_dialog_filter_date.view.*


/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class DialogFilterDateView : FrameLayout {

    var click: OnClickInterfaceObject<String>? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_dialog_filter_date, this)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        startDateButton.setOnClickListener {
            UtilsHelper.showDateDialog(context, object : OnClickInterfaceObject<String> {
                override fun dataClicked(data: String) {
                    startDateTextView.text = data
                }
            })
        }

        endDateButton.setOnClickListener {
            UtilsHelper.showDateDialog(context, object : OnClickInterfaceObject<String> {
                override fun dataClicked(data: String) {
                    endDateTextView.text = data
                }
            })
        }

        filterButton.setOnClickListener {
            if (startDateTextView.text == "-" || endDateTextView.text == "-") {
                return@setOnClickListener
            }
            click?.dataClicked("${startDateTextView.text},${endDateTextView.text}")
        }

    }

    fun setClicked(clicked: OnClickInterfaceObject<String>?) {
        click = clicked
    }

}