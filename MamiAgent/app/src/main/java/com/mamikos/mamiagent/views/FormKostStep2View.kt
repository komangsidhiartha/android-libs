package com.mamikos.mamiagent.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.entities.AreaEntity
import com.mamikos.mamiagent.helpers.NumberTextWatcher
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import com.mamikos.mamiagent.networks.responses.AreaResponse
import kotlinx.android.synthetic.main.activity_form_kost.*
import kotlinx.android.synthetic.main.view_btn_back_next.view.*
import kotlinx.android.synthetic.main.view_form_kost_step_2.view.*
import org.jetbrains.anko.onCheckedChange

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class FormKostStep2View : FrameLayout {

    private lateinit var nextClick: Runnable
    private lateinit var backClick: Runnable
    private var scrollView: LockableScrollView? = null
    var typeGender = "0"
    var roomSize = "3,3"
    var isElectricity = ""
    var minPaySelected = ""

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

        setupElectricity()

        viewBtnBackNextStep2.nextLinearLayout.setOnClickListener {
            if (scrollView == null) {
                scrollView = (context as Activity).formKostScrollView
            }
            //validation()
            nextClick.run()
        }

        viewBtnBackNextStep2.backLinearLayout.setOnClickListener {
            backClick.run()
        }

        dayPayEditText.addTextChangedListener(NumberTextWatcher(dayPayEditText, UtilsHelper.KEY_FORMAT_PRICE))
        weekPayEditText.addTextChangedListener(NumberTextWatcher(weekPayEditText, UtilsHelper.KEY_FORMAT_PRICE))
        monthPayEditText.addTextChangedListener(NumberTextWatcher(monthPayEditText, UtilsHelper.KEY_FORMAT_PRICE))
        yearPayEditText.addTextChangedListener(NumberTextWatcher(yearPayEditText, UtilsHelper.KEY_FORMAT_PRICE))

    }

    private fun setupElectricity() {
        includeElectricityRadioButton.onCheckedChange { _, b ->
            if (b) {
                isElectricity = "0"
            }
        }

        withoutElectricityRadioButton.onCheckedChange { _, b ->
            if (b) {
                isElectricity = "1"
            }
        }
    }

    private fun validation() {
        if (kosNameEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Data nama kos tidak boleh kosong")
            UtilsHelper.autoFocusScroll(kosNameEditText, scrollView)
            return
        }

        if (roomTotalEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Data total kamar tidak boleh kosong")
            UtilsHelper.autoFocusScroll(roomTotalEditText, scrollView)
            return
        }

        if (roomTotalNowEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Data kamar kosong tidak boleh kosong")
            UtilsHelper.autoFocusScroll(roomTotalNowEditText, scrollView)
            return
        }

        if (dayPayEditText.text.toString().isEmpty() && weekPayEditText.text.toString().isEmpty() && monthPayEditText.text.toString().isEmpty() && yearPayEditText.text.toString().isEmpty()) {
            UtilsHelper.showSnackbar(this, "Data pembayaran tidak boleh kosong")
            UtilsHelper.autoFocusScroll(paymentPeriodBy, scrollView)
            return
        }

        if (minPaySpinnerCustomView.getDataSelected() == null) {
            UtilsHelper.showSnackbar(this, "Data minimal pembayaran tidak boleh kosong")
            UtilsHelper.autoFocusScroll(roomPriceTitleTextView, scrollView)
            return
        }

        if (roomSize.isEmpty() || !roomSize.contains(",")) {
            UtilsHelper.showSnackbar(this, "Data ukuran kamar tidak boleh kosong")
            UtilsHelper.autoFocusScroll(threeThreeRoomSizeView, scrollView)
            return
        }

        nextClick.run()

    }

    private fun setupMinPay() {
        minPaySpinnerCustomView.setHint(context.getString(R.string.msg_min_pay))
        val minPay = resources.getStringArray(R.array.min_bayar_room)
        val minPayId = resources.getStringArray(R.array.id_min_bayar_room)

        val dataResponse = AreaResponse()
        val data: ArrayList<AreaEntity> = arrayListOf()

        for (i in 0 until minPay.size) {
            val newData = AreaEntity(id = minPayId[i].toInt(), name = minPay[i])
            data.add(newData)
        }

        dataResponse.data.addAll(data)

        minPaySpinnerCustomView.setData(dataResponse)
        minPaySpinnerCustomView.setClick(object : OnClickInterfaceObject<AreaEntity> {
            override fun dataClicked(data: AreaEntity) {
                minPaySpinnerCustomView.setName(data.name)
                minPaySelected = data.id.toString()
            }
        })

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
            roomSize = "3,3"
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
            roomSize = "3,4"
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
            roomSize = "4,4"
        })

        customRoomSizeView.setRoomSize()
        customRoomSizeView.setTextRoomSize("", "")
        customRoomSizeView.setCheckList(false)
        customRoomSizeView.setOnClick(Runnable {
            threeThreeRoomSizeView.setCheckList(false)
            threeFourRoomSizeView.setCheckList(false)
            fourFourRoomSizeView.setCheckList(false)
            customRoomSizeView.setCheckList(true)
            roomSize = customRoomSizeView.getTextRoomSize()
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
            typeGender = "0"
        })

        genderWomenTypeKosView.setString(context.getString(R.string.msg_women))
        genderWomenTypeKosView.setImage(R.drawable.ic_girl)
        genderWomenTypeKosView.setCheckList(false)
        genderWomenTypeKosView.setOnClick(Runnable {
            genderMixTypeKosView.setCheckList(false)
            genderMenTypeKosView.setCheckList(false)
            genderWomenTypeKosView.setCheckList(true)
            typeGender = "1"
        })

        genderMixTypeKosView.setString(context.getString(R.string.msg_mix))
        genderMixTypeKosView.setImage(R.drawable.ic_noun_boy_and_girl)
        genderMixTypeKosView.setCheckList(false)
        genderMixTypeKosView.setOnClick(Runnable {
            genderMixTypeKosView.setCheckList(true)
            genderMenTypeKosView.setCheckList(false)
            genderWomenTypeKosView.setCheckList(false)
            typeGender = "2"
        })
    }

    fun setNextOnClick(click: Runnable) {
        nextClick = click
    }

    fun setBackOnClick(click: Runnable) {
        backClick = click
    }

}