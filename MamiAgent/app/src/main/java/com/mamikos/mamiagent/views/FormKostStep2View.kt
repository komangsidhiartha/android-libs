package com.mamikos.mamiagent.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import kotlinx.android.synthetic.main.view_btn_back_next.view.*
import kotlinx.android.synthetic.main.view_form_kost_step_2.view.*

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class FormKostStep2View : FrameLayout {

    private lateinit var nextClick: Runnable
    private lateinit var backClick: Runnable

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_form_kost_step_2, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        setupGender()

        setupRoomSize()

        setupPrice()

        setupMinPay()

        viewBtnBackNextStep2.nextLinearLayout.setOnClickListener {
            nextClick.run()
        }

        viewBtnBackNextStep2.backLinearLayout.setOnClickListener {
            backClick.run()
        }

    }

    private fun setupMinPay() {
        minPaySpinnerCustomView.setHint(context.getString(R.string.msg_min_pay))
    }

    private fun setupPrice() {
        monthSquareGreyView.setCheckList(false)
        monthSquareGreyView.setJustText()
        monthSquareGreyView.setString(context.getString(R.string.msg_pay_month))
        monthSquareGreyView.setOnClick(Runnable {
            if (!monthSquareGreyView.isChecked) {
                monthLinearLayout.visibility = View.VISIBLE
                monthSquareGreyView.setCheckList(true)
            } else {
                monthLinearLayout.visibility = View.GONE
                monthSquareGreyView.setCheckList(false)
            }
            monthPayEditText.setText("")
            showTitleRoomPrice()
        })

        daySquareGreyView.setCheckList(false)
        daySquareGreyView.setJustText()
        daySquareGreyView.setString(context.getString(R.string.msg_pay_day))
        daySquareGreyView.setOnClick(Runnable {
            if (!daySquareGreyView.isChecked) {
                dayLinearLayout.visibility = View.VISIBLE
                daySquareGreyView.setCheckList(true)
            } else {
                dayLinearLayout.visibility = View.GONE
                daySquareGreyView.setCheckList(false)
            }
            dayPayEditText.setText("")
            showTitleRoomPrice()
        })

        yearSquareGreyView.setCheckList(false)
        yearSquareGreyView.setJustText()
        yearSquareGreyView.setString(context.getString(R.string.msg_pay_year))
        yearSquareGreyView.setOnClick(Runnable {
            if (!yearSquareGreyView.isChecked) {
                yearLinearLayout.visibility = View.VISIBLE
                yearSquareGreyView.setCheckList(true)
            } else {
                yearLinearLayout.visibility = View.GONE
                yearSquareGreyView.setCheckList(false)
            }
            yearPayEditText.setText("")
            showTitleRoomPrice()
        })

        weekSquareGreyView.setCheckList(false)
        weekSquareGreyView.setJustText()
        weekSquareGreyView.setString(context.getString(R.string.msg_pay_week))
        weekSquareGreyView.setOnClick(Runnable {
            if (!weekSquareGreyView.isChecked) {
                weekLinearLayout.visibility = View.VISIBLE
                weekSquareGreyView.setCheckList(true)
            } else {
                weekLinearLayout.visibility = View.GONE
                weekSquareGreyView.setCheckList(false)
            }
            weekPayEditText.setText("")
            showTitleRoomPrice()
        })
    }

    private fun showTitleRoomPrice() {
        if (!monthSquareGreyView.isChecked && !daySquareGreyView.isChecked && !weekSquareGreyView.isChecked && !yearSquareGreyView.isChecked) {
            roomPriceTitleTextView.visibility = View.GONE
        } else {
            roomPriceTitleTextView.visibility = View.VISIBLE
        }
    }

    private fun setupRoomSize() {
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
    }

    private fun setupGender() {
        genderMenTypeKosView.setString(context.getString(R.string.msg_man))
        genderMenTypeKosView.setImage(R.drawable.ic_boy)
        genderMenTypeKosView.setCheckList(true)
        genderMenTypeKosView.setOnClick(Runnable {
            genderMixTypeKosView.setCheckList(false)
            genderMenTypeKosView.setCheckList(true)
            genderWomenTypeKosView.setCheckList(false)
        })

        genderWomenTypeKosView.setString(context.getString(R.string.msg_women))
        genderWomenTypeKosView.setImage(R.drawable.ic_girl)
        genderWomenTypeKosView.setCheckList(false)
        genderWomenTypeKosView.setOnClick(Runnable {
            genderMixTypeKosView.setCheckList(false)
            genderMenTypeKosView.setCheckList(false)
            genderWomenTypeKosView.setCheckList(true)
        })

        genderMixTypeKosView.setString(context.getString(R.string.msg_mix))
        genderMixTypeKosView.setImage(R.drawable.ic_noun_boy_and_girl)
        genderMixTypeKosView.setCheckList(false)
        genderMixTypeKosView.setOnClick(Runnable {
            genderMixTypeKosView.setCheckList(true)
            genderMenTypeKosView.setCheckList(false)
            genderWomenTypeKosView.setCheckList(false)
        })
    }

    fun setNextOnClick(click: Runnable) {
        nextClick = click
    }

    fun setBackOnClick(click: Runnable) {
        backClick = click
    }

}