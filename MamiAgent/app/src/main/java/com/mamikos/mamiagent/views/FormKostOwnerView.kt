package com.mamikos.mamiagent.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.mamikos.mamiagent.helpers.isValidPhone
import kotlinx.android.synthetic.main.activity_form_kost.*
import kotlinx.android.synthetic.main.view_btn_back_next.view.*
import kotlinx.android.synthetic.main.view_form_kost_owner.view.*

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class FormKostOwnerView : FrameLayout {

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
        inflate(context, R.layout.view_form_kost_owner, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        btnBackNextOwnerView.nextLinearLayout.setOnClickListener {
            if (scrollView == null) {
                scrollView = (context as Activity).formKostScrollView
            }

            validation()
            //nextClick.run()
        }

        btnBackNextOwnerView.backLinearLayout.setOnClickListener {
            backClick.run()
        }

    }

    private fun validation() {
        if (ownerNameEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Data nama pemilik kos tidak boleh kosong")
            UtilsHelper.autoFocusScroll(ownerNameEditText, scrollView)
            return
        }

        if (ownerNameEditText.text.toString().length < 6) {
            UtilsHelper.showSnackbar(this, "Nama pemilik minimal 6 karakter")
            UtilsHelper.autoFocusScroll(ownerNameEditText, scrollView)
            return
        }

        if (ownerPhoneEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Data nomor telepon pemilik kos tidak boleh kosong")
            UtilsHelper.autoFocusScroll(ownerPhoneEditText, scrollView)
            return
        }


        /*if (ownerPasswordEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Password tidak boleh kosong")
            UtilsHelper.autoFocusScroll(ownerPasswordEditText, scrollView)
            return
        }

        if (ownerRepeatPasswordEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Ulangi Password tidak boleh kosong")
            UtilsHelper.autoFocusScroll(ownerRepeatPasswordEditText, scrollView)
            return
        }

        if (!ownerPasswordEditText.text.toString().contentEquals(ownerRepeatPasswordEditText.text.toString())) {
            UtilsHelper.showSnackbar(this, "Password tidak sama")
            UtilsHelper.autoFocusScroll(ownerPasswordEditText, scrollView)
            return
        }*/

        if (!privacyCheckBox.isChecked) {
            UtilsHelper.showSnackbar(this, "Kamu belum setuju dengan syarat yang berlaku")
            UtilsHelper.autoFocusScroll(privacyCheckBox, scrollView)
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


}