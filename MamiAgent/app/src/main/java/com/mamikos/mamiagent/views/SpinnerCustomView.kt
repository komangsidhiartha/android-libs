package com.mamikos.mamiagent.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.helpers.UtilsHelper
import kotlinx.android.synthetic.main.view_field_location.view.*
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.mamikos.mamiagent.entities.AreaEntity
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import com.mamikos.mamiagent.networks.responses.AreaResponse

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class SpinnerCustomView : FrameLayout {

    var popUpWindow: PopupWindow? = null
    private var areaSelected: AreaEntity? = null
    private var response: AreaResponse? = null
    private var onClick: OnClickInterfaceObject<AreaEntity>? = null

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

            UtilsHelper.hideSoftInput(context as Activity)

            if (response == null || response?.data?.size == 0) {
                Toast.makeText(context, "Data kosong, coba klik lagi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val v = CustomRecyclerView(context)
            v.setAreaOnClick(object : OnClickInterfaceObject<AreaEntity> {
                override fun dataClicked(data: AreaEntity) {
                    UtilsHelper.log("data ${data.name}")
                    popUpWindow?.dismiss()
                    onClick?.dataClicked(data)
                    areaSelected = data
                }
            })
            v.setData(response?.data)
            popUpWindow = PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true) // Creation of popup
            popUpWindow?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent))
            popUpWindow?.isOutsideTouchable = false
            popUpWindow?.showAsDropDown(it)
        }
    }

    fun setClick(clicked: OnClickInterfaceObject<AreaEntity>?) {
        onClick = clicked
    }

    fun setHint(strHint: String) {
        locationHintTextView.text = strHint
    }

    fun setName(strName: String) {
        locationNameTextView.text = strName
    }

    fun getName(): String {
        return locationNameTextView.text.toString()
    }

    fun setData(data: AreaResponse?) {
        response = data
        areaSelected = null
    }

    fun getDataSelected(): AreaEntity? {
        return areaSelected
    }


}