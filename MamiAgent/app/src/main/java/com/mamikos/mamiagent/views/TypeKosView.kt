package com.mamikos.mamiagent.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import kotlinx.android.synthetic.main.view_kos_type.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.imageResource

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class TypeKosView : FrameLayout {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_kos_type, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setTextGenderType(str: String) {
        genderTypeTextView.text = str
    }

    fun setImageGenderType(img: Int) {
        typeGenderImageView.imageResource = img
    }

    fun setCheckList(isOkay: Boolean) {
        if (isOkay) {
            checkListGenderImageView.visibility = View.VISIBLE
            contentTypeKosConstraintLayout.backgroundResource = R.drawable.border_radius_type_kos_green
            genderTypeTextView.setTextColor(ContextCompat.getColor(context, R.color.apptheme_color))
        } else {
            checkListGenderImageView.visibility = View.GONE
            contentTypeKosConstraintLayout.backgroundResource = R.drawable.border_radius_type_kos_grey
            genderTypeTextView.setTextColor(ContextCompat.getColor(context, R.color.silver_approx_2))
        }
    }

}