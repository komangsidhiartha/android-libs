package com.sidhiartha.libs.adapters

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by sidhiartha on 28/02/18.
 */
abstract class RecyclerAdapter<E, V : RecyclerAdapter<E, V>.BaseViewHolder>(protected val context: Context, protected var items: MutableList<E>) : RecyclerView.Adapter<V>()
{
    var isLoading = false
    var needToLoadMore = true

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(OnScrollListener())
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    override fun onBindViewHolder(holder: V, position: Int)
    {
        holder.bind(items[position])
    }

    open fun addItem(item: E) {
        items.add(item)
        notifyDataSetChanged()
    }

    open fun addItems(newItems : List<E>)
    {
        if (newItems.isEmpty())
        {
            needToLoadMore = false
            return
        }

        val tempItem = arrayListOf<E>()
        tempItem.addAll(items)
        tempItem.addAll(newItems)
        items = tempItem
        notifyDataSetChanged()
    }

    abstract fun loadMore()

    inner class OnScrollListener : RecyclerView.OnScrollListener()
    {
        private val visibleThreshold = 2

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
        {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
        {
            super.onScrolled(recyclerView, dx, dy)

            if (isLoading || !needToLoadMore)
                return

            val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
            val lastVisibleItemPosition = when
            {
                recyclerView.layoutManager is LinearLayoutManager -> (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                recyclerView.layoutManager is GridLayoutManager -> (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                else -> 0
            }

            if (totalItemCount <= (lastVisibleItemPosition + visibleThreshold))
                loadMore()
        }
    }

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        abstract fun bind(item: E);
    }
}