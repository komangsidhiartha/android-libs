package com.mamikos.mamiagent.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import android.view.View
import android.widget.ImageView
import com.git.dabang.database.table.FormDataTable
import kotlinx.android.synthetic.main.view_list_data_form.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class ListDataFormView : FrameLayout {

    var viewHolder: ViewHolder? = null
    var runnable: Runnable? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_list_data_form, this)
        viewHolder = ViewHolder(this)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun setData(data: FormDataTable?) {
            nameKosTextView?.text = data?.kosName
            ownerNameTextView?.text = data?.ownerName
            ownerPhoneTextView?.text = data?.ownerPhone
            addressTextView?.text = data?.address

            data?.photoBathroomBuilding?.isNotEmpty().let {
                val pathImage = data?.photoBathroomBuilding?.split(",")
                LoadBitmap(picBathRoomImageView).execute(pathImage?.get(1))
            }

            data?.photoInsideBuilding?.isNotEmpty().let {
                val pathImage = data?.photoInsideBuilding?.split(",")
                LoadBitmap(picInsideKosImageView).execute(pathImage?.get(1))
            }

            data?.photoKosBuildingBuilding?.isNotEmpty().let {
                val pathImage = data?.photoKosBuildingBuilding?.split(",")
                LoadBitmap(picBuildingKosImageView).execute(pathImage?.get(1))
            }

            sendListDataButton.setOnClickListener {
                EventBus.getDefault().post(data)
            }

        }
    }

    @SuppressLint("StaticFieldLeak") private inner class LoadBitmap(imageView: ImageView?) :
            AsyncTask<String, Void, Bitmap>() {

        var img = imageView

        override fun doInBackground(vararg path: String): Bitmap? {
            val options = BitmapFactory.Options()
            options.inSampleSize = 4
            return BitmapFactory.decodeFile(path[0], options)
        }

        override fun onPostExecute(bitmap: Bitmap) {
            img?.setImageBitmap(bitmap)
        }
    }

}