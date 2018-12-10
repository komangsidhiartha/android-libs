package com.mamikos.mamiagent.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.entities.AreaEntity
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import com.mamikos.mamiagent.networks.responses.AreaResponse
import kotlinx.android.synthetic.main.activity_form_kost.*
import kotlinx.android.synthetic.main.view_btn_back_next.view.*
import kotlinx.android.synthetic.main.view_form_kost_location.view.*

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class FormKostLocationView : FrameLayout {

    private lateinit var nextClick: Runnable
    private lateinit var backClick: Runnable
    private var scrollView: LockableScrollView? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_form_kost_location, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        citySpinnerCustomView.setHint(context.getString(R.string.msg_city_star))
        districtSpinnerCustomView.setHint(context.getString(R.string.msg_district_star))

        btnBackNextLocationView.nextLinearLayout.setOnClickListener {
            if (scrollView == null) {
                scrollView = (context as Activity).formKostScrollView
            }
            validation()
            //nextClick.run()
        }

        btnBackNextLocationView.backLinearLayout.setOnClickListener {
            backClick.run()
        }
    }

    private fun validation() {
        if (provinceSpinnerCustomView?.getDataSelected() == null) {
            UtilsHelper.showSnackbar(this, "Data provinsi tidak boleh kosong")
            UtilsHelper.autoFocusScroll(provinceSpinnerCustomView, scrollView)
            return
        }

        if (citySpinnerCustomView?.getDataSelected() == null) {
            UtilsHelper.showSnackbar(this, "Data kota tidak boleh kosong")
            UtilsHelper.autoFocusScroll(citySpinnerCustomView, scrollView)
            return
        }

        if (districtSpinnerCustomView?.getDataSelected() == null) {
            UtilsHelper.showSnackbar(this, "Data kecamatan tidak boleh kosong")
            UtilsHelper.autoFocusScroll(districtSpinnerCustomView, scrollView)
            return
        }

        if (fullAddressEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Data alamat tidak boleh kosong")
            UtilsHelper.autoFocusScroll(fullAddressEditText, scrollView)
            return
        }

        if (locationEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Data peta tidak boleh kosong")
            UtilsHelper.autoFocusScroll(locationEditText, scrollView)
            return
        }

        nextClick.run()

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