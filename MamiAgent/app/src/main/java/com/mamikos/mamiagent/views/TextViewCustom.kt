package com.mamikos.mamiagent.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import android.view.View
import kotlinx.android.synthetic.main.text_view_custom.view.*


/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class TextViewCustom : FrameLayout {

    lateinit var viewHolder: ViewHolder
    lateinit var runnable: Runnable

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.text_view_custom, this)
        viewHolder = ViewHolder(this)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        viewHolder.textView?.setOnClickListener {
            runnable.run()
        }
    }

    fun removeBackground() {
        viewHolder.textView?.setBackgroundResource(0)
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView?.txtViewCustom
    }

}