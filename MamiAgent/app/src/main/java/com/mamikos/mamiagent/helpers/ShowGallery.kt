package com.mamikos.mamiagent.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent

import com.mamikos.mamiagent.R

import java.io.File

/**
 * Created by Dedi Android on 28/03/2018.
 * Happy coding, buddy!
 */

class ShowGallery(private val mContext: Context) {

    private val imgFromCamera: File? = null

    fun checkPermission() {
        UtilsPermission.checkPermissionStorageAndCamera(mContext as Activity)
    }

    fun showNow() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/"
        (mContext as Activity).startActivityForResult(Intent.createChooser(intent, mContext.getString(R.string.msg_select_file)), CODE_GALLERY)
    }

    companion object {
        val CODE_GALLERY = 64
    }


}
