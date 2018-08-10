package com.mamikos.mamiagent.items

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mamikos.mamiagent.entities.MediaEntity
import com.sidhiartha.libs.apps.logIfDebug
import kotlinx.android.synthetic.main.item_add_photo.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.onClick

class AddPhotoItem(view: View) {
    lateinit var mediaEntity: MediaEntity
    var view = view

    fun bind(media: MediaEntity, uploader: String, onAdd: () -> Unit, onDelete: (MediaEntity, String) -> Unit) {
        this.mediaEntity = media
        view.rlAddPhoto.onClick { onAdd() }
        view.btAddPhoto.onClick { onAdd() }
        view.ivDeletePhoto.onClick { onDelete(media, uploader) }
        if (media.id < 0)
            initiateDefault()
        else {
            view.llThumbnail.visibility = View.VISIBLE
            view.ivThumbnail.visibility = View.VISIBLE
            view.ivThumbnail.backgroundColor = android.R.color.transparent
            Glide.with(view).load(media.photo).apply(RequestOptions().transform(RoundedCorners(10))).into(view.ivThumbnail)
            view.rlAddPhoto.visibility = View.GONE
            view.btAddPhoto.visibility = View.GONE
            view.ivDeletePhoto.visibility = View.VISIBLE
        }
    }

    private fun initiateDefault() {
        /*Item AddPhoto From Start*/
        view.llThumbnail.visibility = View.GONE
        view.ivThumbnail.visibility = View.GONE
        view.rlAddPhoto.visibility = View.VISIBLE
        view.btAddPhoto.visibility = View.VISIBLE
        view.ivDeletePhoto.visibility = View.GONE
    }
}