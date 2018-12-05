package com.mamikos.mamiagent.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.adapters.ListDataFormAdapter
import com.mamikos.mamiagent.apps.MamiApp
import com.mamikos.mamiagent.helpers.UtilsHelper
import com.sidhiartha.libs.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_list_data_form.*
import android.support.v7.widget.LinearLayoutManager
import com.git.dabang.database.table.FormDataTable

/**
 * Created by Dedi Dot on 12/4/2018.
 * Happy Coding!
 */

class ListDataFormActivity : BaseActivity() {

    var dataKos: List<FormDataTable>? = null
    var adapter: ListDataFormAdapter? = null

    override val layoutResource: Int = R.layout.activity_list_data_form

    override fun viewDidLoad() {
        loading?.show()

        adapter = ListDataFormAdapter()
        dataFormRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dataFormRecyclerView.adapter = adapter

        LoadDataLocal().execute()

        UtilsHelper.setupToolbar(this, dataFormToolbar, getString(R.string.msg_local_data), "", R.drawable.ic_arrow_back_white_24dp, Runnable {
            onBackPressed()
        })
    }

    @SuppressLint("StaticFieldLeak") private inner class LoadDataLocal :
            AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg dataDoInBackground: String): String? {
            //dataKos = MamiApp.instance?.appDatabase?.formDataDao()?.getAll!!
            dataKos = MamiApp.instance?.appDatabase?.formDataDao()?.getDataByPage("500", "700")
            adapter?.setData(dataKos)
            return ""
        }

        override fun onPostExecute(z: String) {
            adapter?.notifyDataSetChanged()
            loading?.hide()
        }
    }

}