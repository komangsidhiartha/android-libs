package com.mamikos.mamiagent.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.adapters.ListDataHistoryAdapter
import com.mamikos.mamiagent.entities.HistoryEntity
import com.mamikos.mamiagent.helpers.ExceptionHandler
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.mamikos.mamiagent.networks.apis.AgentHistoryApi
import com.mamikos.mamiagent.networks.apis.TelegramApi
import com.mamikos.mamiagent.networks.responses.MessagesResponse
import com.mamikos.mamiagent.networks.responses.RoomResponse
import com.sidhiartha.libs.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_list_data_form.*
import android.view.Menu
import android.view.MenuItem


/**
 * Created by Dedi Dot on 12/7/2018.
 * Happy Coding!
 */

class ListAgentHistoryActivity : BaseActivity() {

    var adapter: ListDataHistoryAdapter? = null
    var data: ArrayList<HistoryEntity> = ArrayList()
    var page = 1

    override val layoutResource: Int = R.layout.activity_list_data_form

    override fun viewDidLoad() {

        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))

        adapter = ListDataHistoryAdapter(this, data, {
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
                UtilsHelper.showDialogYes(this, "", "Server lagi error, hubungi developer", Runnable {
                    finish()
                }, 0)
                return@exec
            }

            if (page == 1) {
                data.clear()
            }

            if (response.status) {
                data.addAll(response.data)
                adapter?.notifyDataSetChanged()
                page += 1
                if (response.data.size < 10) {
                    adapter?.needToLoadMore = false
                }
            } else {
                sendReport(errorMessage.toString())
                adapter?.needToLoadMore = false
            }

            loadingFrameLayout.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            adapter?.isLoading = false

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_agen_history_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.filterMenu) {
            return false
        }
        return true
    }

    private fun sendReport(s: String) {
        val reportApi = TelegramApi.SendReport(s)
        reportApi.exec(MessagesResponse::class.java) { _: MessagesResponse?, _: String? -> }
    }

}