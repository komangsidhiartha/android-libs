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

        genderMenTypeKosView.setString(context.getString(R.string.msg_man))
        genderMenTypeKosView.setImage(R.drawable.ic_boy)
        genderMenTypeKosView.setCheckList(true)
        genderMenTypeKosView.setOnClickListener {
            genderMixTypeKosView.setCheckList(false)
            genderMenTypeKosView.setCheckList(true)
            genderWomenTypeKosView.setCheckList(false)
        }

        genderWomenTypeKosView.setString(context.getString(R.string.msg_women))
        genderWomenTypeKosView.setImage(R.drawable.ic_girl)
        genderWomenTypeKosView.setCheckList(false)
        genderWomenTypeKosView.setOnClickListener {
            genderMixTypeKosView.setCheckList(false)
            genderMenTypeKosView.setCheckList(false)
            genderWomenTypeKosView.setCheckList(true)
        }

        genderMixTypeKosView.setString(context.getString(R.string.msg_mix))
        genderMixTypeKosView.setImage(R.drawable.ic_noun_boy_and_girl)
        genderMixTypeKosView.setCheckList(false)
        genderMixTypeKosView.setOnClickListener {
            genderMixTypeKosView.setCheckList(true)
            genderMenTypeKosView.setCheckList(false)
            genderWomenTypeKosView.setCheckList(false)
        }

        threeThreeRoomSizeView.setRoomSize()
        threeThreeRoomSizeView.setTextRoomSize("3", "3")
        threeThreeRoomSizeView.removeBackgroundRoomSize()
        threeThreeRoomSizeView.setCheckList(true)
        threeThreeRoomSizeView.setOnClick(Runnable {
            threeThreeRoomSizeView.setCheckList(true)
            threeFourRoomSizeView.setCheckList(false)
            fourFourRoomSizeView.setCheckList(false)
            customRoomSizeView.setCheckList(false)
        })

        threeFourRoomSizeView.setRoomSize()
        threeFourRoomSizeView.setTextRoomSize("3", "4")
        threeFourRoomSizeView.removeBackgroundRoomSize()
        threeFourRoomSizeView.setCheckList(false)
        threeFourRoomSizeView.setOnClick(Runnable {
            threeThreeRoomSizeView.setCheckList(false)
            threeFourRoomSizeView.setCheckList(true)
            fourFourRoomSizeView.setCheckList(false)
            customRoomSizeView.setCheckList(false)
        })

        fourFourRoomSizeView.setRoomSize()
        fourFourRoomSizeView.setTextRoomSize("4", "4")
        fourFourRoomSizeView.removeBackgroundRoomSize()
        fourFourRoomSizeView.setCheckList(false)
        fourFourRoomSizeView.setOnClick(Runnable {
            threeThreeRoomSizeView.setCheckList(false)
            threeFourRoomSizeView.setCheckList(false)
            fourFourRoomSizeView.setCheckList(true)
            customRoomSizeView.setCheckList(false)
        })

        customRoomSizeView.setRoomSize()
        customRoomSizeView.setTextRoomSize("", "")
        customRoomSizeView.setCheckList(false)
        customRoomSizeView.setOnClick(Runnable {
            threeThreeRoomSizeView.setCheckList(false)
            threeFourRoomSizeView.setCheckList(false)
            fourFourRoomSizeView.setCheckList(false)
            customRoomSizeView.setCheckList(true)
        })



        monthSquareGreyView.setJustText()
        monthSquareGreyView.setString(context.getString(R.string.msg_pay_month))

        daySquareGreyView.setJustText()
        daySquareGreyView.setString(context.getString(R.string.msg_pay_day))

        yearSquareGreyView.setJustText()
        yearSquareGreyView.setString(context.getString(R.string.msg_pay_year))

        weekSquareGreyView.setJustText()
        weekSquareGreyView.setString(context.getString(R.string.msg_pay_week))


    }


}