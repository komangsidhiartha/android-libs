package com.mamikos.mamiagent.adapters

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.mamikos.mamiagent.R
import com.mamikos.mamiagent.entities.MediaEntity
import com.mamikos.mamiagent.items.AddPhotoItem
import com.mamikos.mamiagent.views.inflate
import org.jetbrains.anko.layoutInflater
import java.util.ArrayList
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import com.sidhiartha.libs.apps.logIfDebug


class AddPhotoAdapter(val context: Context, val mediaEntities: ArrayList<MediaEntity>, val uploader: String,
                      val onAddPhoto: (String) -> Unit, val onDeleteMedia: (MediaEntity, String) -> Unit): BaseAdapter() {
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setNewMedia(media: MediaEntity, upload: String) {
        if (uploader == upload) {
            mediaEntities.add(mediaEntities.size - 1, media)
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return mediaEntities.size
    }

    override fun getItem(i: Int): MediaEntity {
        return mediaEntities[i]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {
            view = context.layoutInflater.inflate(R.layout.item_add_photo, parent, false)
        }

        var addPhotoItem: AddPhotoItem? = view?.tag as? AddPhotoItem
        if (addPhotoItem == null) {
            addPhotoItem = view?.let { AddPhotoItem(it) }
            view?.setTag(addPhotoItem)
        }
        addPhotoItem?.bind(getItem(position), uploader, {
            onAddPhoto(uploader)
        }, { entity: MediaEntity, type: String ->
            onDeleteMedia(entity, type)
            notifyDataSetChanged()
        })
        return view
    }


    fun refresh() {
        for (i in mediaEntities.indices) {
            //int abc = mediaEntities.size()-1;
            if (i != mediaEntities.size) {
                mediaEntities.removeAt(i)
            }
        }
        notifyDataSetChanged()
    }
}