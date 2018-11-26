package com.mamikos.mamiagent.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.entities.AreaEntity
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import com.mamikos.mamiagent.networks.responses.AreaResponse
import kotlinx.android.synthetic.main.view_btn_back_next.view.*
import kotlinx.android.synthetic.main.view_form_kost_step_1.view.*

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class FormKostStep1View : FrameLayout {

    private lateinit var nextClick: Runnable
    private lateinit var backClick: Runnable

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_form_kost_step_1, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        citySpinnerCustomView.setHint(context.getString(R.string.msg_city_star))
        districtSpinnerCustomView.setHint(context.getString(R.string.msg_district_star))

        viewBtnBackNextStep1.nextLinearLayout.setOnClickListener {
            nextClick.run()
        }

        viewBtnBackNextStep1.backLinearLayout.setOnClickListener {
            backClick.run()
        }
    }

    fun setNextOnClick(click: Runnable) {
        nextClick = click
    }

    fun setBackOnClick(click: Runnable) {
        backClick = click
    }

    fun setProvince(response: AreaResponse?, click: OnClickInterfaceObject<AreaEntity>?) {
        provinceSpinnerCustomView.setData(response)
        provinceSpinnerCustomView.setClick(click)
    }

    fun setCity(response: AreaResponse?, click: OnClickInterfaceObject<AreaEntity>?) {
        citySpinnerCustomView.setData(response)
        citySpinnerCustomView.setClick(click)
    }

    fun setSubdistrict(response: AreaResponse?, click: OnClickInterfaceObject<AreaEntity>?) {
        districtSpinnerCustomView.setData(response!!)
        districtSpinnerCustomView.setClick(click!!)
    }


}