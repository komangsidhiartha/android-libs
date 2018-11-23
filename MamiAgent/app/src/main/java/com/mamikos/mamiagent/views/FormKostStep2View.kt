package com.mamikos.mamiagent.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import kotlinx.android.synthetic.main.view_form_kost_step_2.view.*

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class FormKostStep2View : FrameLayout {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_form_kost_step_2, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        genderMenTypeKosView.setTextGenderType(context.getString(R.string.msg_man))
        genderMenTypeKosView.setImageGenderType(R.drawable.ic_boy)
        genderMenTypeKosView.setCheckList(true)
        genderMenTypeKosView.setOnClickListener {
            genderMixTypeKosView.setCheckList(false)
            genderMenTypeKosView.setCheckList(true)
            genderWomenTypeKosView.setCheckList(false)
        }

        genderWomenTypeKosView.setTextGenderType(context.getString(R.string.msg_women))
        genderWomenTypeKosView.setImageGenderType(R.drawable.ic_girl)
        genderWomenTypeKosView.setCheckList(false)
        genderWomenTypeKosView.setOnClickListener {
            genderMixTypeKosView.setCheckList(false)
            genderMenTypeKosView.setCheckList(false)
            genderWomenTypeKosView.setCheckList(true)
        }

        genderMixTypeKosView.setTextGenderType(context.getString(R.string.msg_mix))
        genderMixTypeKosView.setImageGenderType(R.drawable.ic_noun_boy_and_girl)
        genderMixTypeKosView.setCheckList(false)
        genderMixTypeKosView.setOnClickListener {
            genderMixTypeKosView.setCheckList(true)
            genderMenTypeKosView.setCheckList(false)
            genderWomenTypeKosView.setCheckList(false)
        }




    }


}