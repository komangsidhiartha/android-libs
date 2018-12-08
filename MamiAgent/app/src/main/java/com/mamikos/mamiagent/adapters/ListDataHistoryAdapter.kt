package com.mamikos.mamiagent.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.mamikos.mamiagent.*
import com.mamikos.mamiagent.entities.HistoryEntity
import com.sidhiartha.libs.adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.view_item_history.view.*
import org.jetbrains.anko.*

class ListDataHistoryAdapter(context: Context, items: ArrayList<HistoryEntity>,
                             private val loadNext: () -> Unit,
                             val itemClickListener: (HistoryEntity) -> Unit) :
        RecyclerAdapter<HistoryEntity, ListDataHistoryAdapter.RoomViewHolder>(context, items) {

    override fun loadMore() {
        loadNext()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val v = context.layoutInflater.inflate(R.layout.view_item_history, parent, false)
        return RoomViewHolder(v)
    }

    inner class RoomViewHolder(itemView: View) : BaseViewHolder(itemView) {

        override fun bind(item: HistoryEntity) {
            itemView.onClick { itemClickListener(item) }
            itemView.roomNameTextView.text = item.name
            itemView.ownerPhoneTextView.text = item.ownerPhone
            itemView.statusTextView.text = item.status//

            if (item.status.equals("verified", true)) {
                itemView.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.accent))
            } else {
                itemView.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
            }

            if (item.reason.isNotEmpty()) {
                itemView.rejectTextView.visibility = View.VISIBLE
                itemView.rejectTextView.text = item.reason
            } else {
                itemView.rejectTextView.visibility = View.GONE
            }

        }

    }
}