package com.sidhiartha.libs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by sidhiartha on 28/02/18.
 */
abstract class RecyclerAdapter<E, V: ViewHolder<E>>(private val context: Context, protected var items: ArrayList<E>): RecyclerView.Adapter<V>()
{
    protected abstract fun layoutResource(): Int

    override fun getItemCount(): Int
    {
        return items.size
    }

    override fun onBindViewHolder(holder: V, position: Int)
    {
        holder.bind(items[position])
    }
}

abstract class ViewHolder<in E>(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    abstract fun bind(item: E)
}