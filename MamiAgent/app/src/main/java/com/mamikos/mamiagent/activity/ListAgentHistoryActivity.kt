package com.mamikos.mamiagent.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.adapters.ListDataRoomAdapter
import com.mamikos.mamiagent.entities.RoomEntity
import com.mamikos.mamiagent.helpers.ExceptionHandler
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.mamikos.mamiagent.networks.apis.AgentHistoryApi
import com.mamikos.mamiagent.networks.apis.TelegramApi
import com.mamikos.mamiagent.networks.responses.MessagesResponse
import com.mamikos.mamiagent.networks.responses.RoomResponse
import com.sidhiartha.libs.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_list_data_form.*

/**
 * Created by Dedi Dot on 12/7/2018.
 * Happy Coding!
 */

class ListAgentHistoryActivity : BaseActivity() {

    var adapter: ListDataRoomAdapter? = null
    var data: ArrayList<RoomEntity> = ArrayList()
    var page = 1

    override val layoutResource: Int = R.layout.activity_list_data_form

    override fun viewDidLoad() {

        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))

        adapter = ListDataRoomAdapter(this, data, {

            getData()
        }, {})
        dataFormRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dataFormRecyclerView.adapter = adapter

        UtilsHelper.setupToolbar(this, dataFormToolbar, getString(R.string.msg_data_history_agen), "", R.drawable.ic_arrow_back_white_24dp, Runnable {
            onBackPressed()
        })

        if (!UtilsHelper.isNetworkConnected(this@ListAgentHistoryActivity)) {
            UtilsHelper.showDialogYes(this, "", "Koneksi tidak stabil", Runnable {}, 0)
            return
        } else {
            getData()
        }

        swipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.accent)

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            adapter?.needToLoadMore = true
            getData()
        }

    }

    private fun getData() {
        if (adapter?.isLoading!!) {
            return
        }

        adapter?.isLoading = true
        loadingFrameLayout.visibility = View.VISIBLE

        val historyApi = AgentHistoryApi.GetAgentHistory()
        historyApi.queryParam = mapOf(Pair("page", page.toString())).toList()
        historyApi.exec(RoomResponse::class.java) { response: RoomResponse?, errorMessage: String? ->

            if (response == null) {
                sendReport(errorMessage.toString())
                UtilsHelper.showDialogYes(this, "", "Server lagi error, hubungi pihak developer", Runnable {
                    finish()
                }, 0)
                return@exec
            }

            if (page == 1) {
                data.clear()
            }

            if (response.status && response.rooms.isNotEmpty()) {
                data.addAll(response.rooms)
                adapter?.notifyDataSetChanged()
                page = response.nextPage
            } else {
                sendReport(errorMessage.toString())
                adapter?.needToLoadMore = false
            }

            loadingFrameLayout.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            adapter?.isLoading = false

        }
    }

    private fun sendReport(s: String) {
        val reportApi = TelegramApi.SendReport(s)
        reportApi.exec(MessagesResponse::class.java) { _: MessagesResponse?, _: String? -> }
    }

}