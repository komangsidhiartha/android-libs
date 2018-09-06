package com.mamikos.mamiagent

import android.app.DatePickerDialog
import android.view.View
import android.widget.Button
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.networks.apis.StatisticApi
import com.mamikos.mamiagent.networks.responses.StatisticResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.activity_statistic.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*


class StatisticActivity : BaseActivity()
{
    override val layoutResource: Int = R.layout.activity_statistic
    var fromDate: String? = null
    var toDate: String? = null
    var startCal = Calendar.getInstance()
    var endCal = Calendar.getInstance()

    override fun viewDidLoad()
    {
        setupActionBar()
        startCal.set(Calendar.DATE, -7)
        btnStartDate.onClick { showDatePicker(startCal, btnStartDate) }

        btnEndDate.onClick { showDatePicker(endCal, btnEndDate) }

        btnSearch.onClick {
            if (!btnStartDate.text.toString().equals(R.string.start_date))
                fromDate = btnStartDate.text.toString()
            if (!btnEndDate.text.toString().equals(R.string.end_date))
                toDate = btnEndDate.text.toString()
            loadData()
        }

        loadData()
    }

    private fun setupActionBar() {
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
            toolbar?.setNavigationOnClickListener(View.OnClickListener { view ->
                if (view.id == -1) {
                    onBackPressed()
                }
            })
            toolbar?.title = getString(R.string.statistic_title)
        }
    }

    fun loadData()
    {
        logIfDebug("FROM $fromDate TO $toDate")
        val api = StatisticApi(fromDate, toDate)
        showLoadingBar()
        api.exec(StatisticResponse::class.java) { response: StatisticResponse?, errorMessage: String? ->
            hideLoadingBar()
            when (response) {
                null ->
                    errorMessage?.let { toast(it) }
                else -> {
                    if (response.status)
                        bindView(response)
                    else
                        toast("" + response.message)
                }
            }
        }
    }

    fun bindView(response: StatisticResponse)
    {
        tvAgenName.text = "${response.agentName} - ${MamiApp.sessionManager.agentPhoneNumber}"
        tvDataSubmited.text = "~ Submited   : ${response.data.submit}"
        tvDataVerified.text = "~ Verified       : ${response.data.live}"
        tvDataReject.text   = "~ Reject         : ${response.data.reject}"
    }

    fun showDatePicker(cal: Calendar, button: Button)
    {
        val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "yyyy-MM-dd" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale("ID"))
                    button.text = sdf.format(cal.time)
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        if (button == btnEndDate)
            datePickerDialog.datePicker.minDate = startCal.timeInMillis
        datePickerDialog.show()
    }
}