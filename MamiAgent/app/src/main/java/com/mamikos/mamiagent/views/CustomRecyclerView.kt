package com.mamikos.mamiagent.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.view_custom_recycler.view.*
import android.support.v7.widget.LinearLayoutManager
import com.mamikos.mamiagent.entities.AreaEntity
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject


/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class CustomRecyclerView : FrameLayout {

    private var data: ArrayList<AreaEntity>? = null
    private lateinit var areaEntityOnClick: OnClickInterfaceObject<AreaEntity>

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_custom_recycler, this)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val adapter = Adapter()
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        customRecyclerView.layoutManager = linearLayoutManager
        customRecyclerView.adapter = adapter
    }

    fun setData(newData: ArrayList<AreaEntity>?) {
        data = newData
        customRecyclerView.adapter.notifyDataSetChanged()
    }

    fun setAreaOnClick(clicked: OnClickInterfaceObject<AreaEntity>) {
        areaEntityOnClick = clicked
    }

    inner class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var textViewCustom: TextViewCustom? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            textViewCustom = TextViewCustom(parent.context)
            textViewCustom?.removeBackground()
            return textViewCustom!!.viewHolder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as TextViewCustom.ViewHolder).textView?.text = data?.get(position)?.name
            holder.textView?.setOnClickListener {
                data?.get(position)?.let { it1 ->
                    areaEntityOnClick.dataClicked(it1)
                }
            }
        }

        override fun getItemCount(): Int {
            return data?.size!!
        }
    }

}