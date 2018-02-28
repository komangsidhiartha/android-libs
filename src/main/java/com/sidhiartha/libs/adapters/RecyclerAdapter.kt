package com.sidhiartha.libs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by sidhiartha on 28/02/18.
 */
abstract class RecyclerAdapter<E, out V: ItemView<E>>(protected val context: Context, protected var items: ArrayList<E>): RecyclerView.Adapter<RecyclerAdapter<E,V>.BaseViewHolder>()
{
    protected abstract fun inflateItemView(parent: ViewGroup?): V

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        return BaseViewHolder(inflateItemView(parent))
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int)
    {
        holder.bind(items[position])
    }

    inner class BaseViewHolder(private val view: V): RecyclerView.ViewHolder(view)
    {
        fun bind(item: E)
        {
            view.bind(item)
        }
    }
}

abstract class ItemView<in E>(context: Context) : ViewGroup(context)
{
    abstract fun bind(item: E)
}