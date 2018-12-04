package com.mamikos.mamiagent.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mamikos.mamiagent.views.ListDataFormView


/**
 * Created by Dedi Dot on 12/4/2018.
 * Happy Coding!
 */

class ListDataFormAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listDataFormView: ListDataFormView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        listDataFormView = ListDataFormView(parent.context)
        return listDataFormView.viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }
}