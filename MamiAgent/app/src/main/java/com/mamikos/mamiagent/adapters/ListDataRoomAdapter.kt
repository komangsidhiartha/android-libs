package com.mamikos.mamiagent.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mamikos.mamiagent.*
import com.mamikos.mamiagent.entities.RoomEntity
import com.sidhiartha.libs.adapters.RecyclerAdapter
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.item_room.*
import kotlinx.android.synthetic.main.item_room.view.*
import org.jetbrains.anko.*

class ListDataRoomAdapter(context: Context, items: ArrayList<RoomEntity>, private val loadNext: () -> Unit, val itemClickListener: (RoomEntity) -> Unit) : RecyclerAdapter<RoomEntity, ListDataRoomAdapter.RoomViewHolder>(context, items) {

    override fun loadMore() {
        loadNext()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val v = context.layoutInflater.inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(v)
    }

    inner class RoomViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        override fun bind(item: RoomEntity)
        {
            itemView.onClick { itemClickListener(item) }
            Log.i(javaClass.simpleName, "item $item")
            val ivRoom = itemView.find<ImageView>(R.id.ivRoom)
            val tvRoomName = itemView.find<TextView>(R.id.tvRoomName)
            val tvAddress = itemView.find<TextView>(R.id.tvAddress)
            val tvDistance = itemView.find<TextView>(R.id.tvDistance)
            val tvPhotoCount = itemView.find<TextView>(R.id.tvPhotoCount)
            val tvReviewCount = itemView.find<TextView>(R.id.tvReviewCount)
            val tvNoteReject = itemView.find<TextView>(R.id.tvNoteReject)
            val tvFac = itemView.find<TextView>(R.id.tvFac)

            Glide.with(itemView).load(item.photoUrl.small)
                    .apply(RequestOptions().override(100, 100).transform(CenterInside()).transform(RoundedCorners(10)))
                    .into(ivRoom)
            tvRoomName.text = item.roomTitle
            tvAddress.text = item.address
            tvPhotoCount.text = "Photo : " + item.photoCount
            tvReviewCount.text = "Review : " + item.reviewCount
            tvDistance.textColor = context.resources.getColor(R.color.black)

            if (ListRoomActivity.currentTabSelected == 0)
            {
                tvDistance.text = item.distanceString
                tvPhotoCount.visibility = View.VISIBLE
                tvReviewCount.visibility = View.VISIBLE
                tvNoteReject.visibility = View.GONE
                itemView.ll_item_room.backgroundColor = context.resources.getColor(R.color.white)
            }
            else
            {
                tvDistance.text = "Status: ${item.statuses}"
                tvPhotoCount.visibility = View.GONE
                tvReviewCount.visibility = View.GONE

                logIfDebug("NOTE ${item.note.isNullOrEmpty()} isi ${item.note}")
                if (item.note.isNullOrEmpty())
                    tvNoteReject.visibility = View.GONE
                else {
                    tvNoteReject.visibility = View.VISIBLE
                    tvNoteReject.text = item.note
                }
                setItemBgColor(item, itemView)
            }

            /*var fac  = ""

            for (newData in item.facRoom.indices){
                fac += item.facRoom[newData]+", "
            }

            for (newData in item.facShare.indices){
                fac += item.facShare[newData]+", "
            }

            for (newData in item.facBath.indices){
                fac += item.facBath[newData]+", "
            }

            for (newData in item.facNear.indices){
                fac += item.facNear[newData]+", "
            }

            for (newData in item.facPark.indices){
                fac += item.facPark[newData]+", "
            }

            for (newData in item.facPrice.indices){
                fac += item.facPrice[newData]+", "
            }

            fac += item.facRoomOther+", "
            fac += item.facBathOther+", "

            val re2 = Regex("[^A-Za-z,,]")
            val re3 = Regex("[^A-Za-z,,,]")
            fac = re2.replace(fac, " ")
            fac = re3.replace(fac, " ")*/

            //tvFac.text = fac

        }

        fun setItemBgColor(roomEntity: RoomEntity, view: View)
        {
            when (roomEntity.statuses) {
                RoomEntity.STATUS_DEFAULT -> {
                    view.ll_item_room.backgroundColor = context.resources.getColor(R.color.white)
                }
                RoomEntity.STATUS_CHECKIN -> {
                    view.ll_item_room.backgroundColor = context.resources.getColor(R.color.white)
                }
                RoomEntity.STATUS_PHOTO -> {
                    view.ll_item_room.backgroundColor = context.resources.getColor(R.color.white)
                }
                RoomEntity.STATUS_REVIEW -> {
                    view.ll_item_room.backgroundColor = context.resources.getColor(R.color.white)
                }
                RoomEntity.STATUS_SUBMIT -> {
                    view.ll_item_room.backgroundColor = Color.parseColor("#36707070")
                }
                RoomEntity.STATUS_VALID -> {
                    view.ll_item_room.backgroundColor = Color.parseColor("#1c22ff00")
                    view.tvDistance.textColor = context.resources.getColor(R.color.apptheme_color)
                }
                RoomEntity.STATUS_INVALID -> {
                    view.ll_item_room.backgroundColor = Color.parseColor("#29ff0000")
                    view.tvDistance.textColor = context.resources.getColor(R.color.red)
                }
            }
        }
    }
}