package com.mamikos.mamiagent.views

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.helpers.ShowCamera
import com.mamikos.mamiagent.helpers.ShowGallery
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import kotlinx.android.synthetic.main.view_dialog_select.view.*

/**
 * Created by Dedi Dot on 7/19/2018.
 * Happy Coding!
 */

class DialogOpenFileView : FrameLayout {

    private var built: AlertDialog.Builder? = null
    private var click: OnClickInterfaceObject<Int>? = null
    private var dialog: Dialog? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.view_dialog_select, this)
        built = AlertDialog.Builder(context)
        built!!.setView(this)
        dialog = built!!.create()
        selectCamera1LinearLayout.setOnClickListener {
            click!!.dataClicked(ShowCamera.CODE_CAMERA)
        }
        selectCamera2LinearLayout.setOnClickListener {
            click!!.dataClicked(ShowCamera.CODE_CAMERA_2)
        }
        selectGalleryLinearLayout.setOnClickListener {
            click!!.dataClicked(ShowGallery.CODE_GALLERY)
        }
    }

    fun setOnClick(click: OnClickInterfaceObject<Int>) {
        this.click = click
    }

    fun showDialog() {
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }


}