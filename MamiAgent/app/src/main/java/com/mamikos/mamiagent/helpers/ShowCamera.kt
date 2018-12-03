package com.mamikos.mamiagent.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.mamikos.mamiagent.BuildConfig

import com.mamikos.mamiagent.activity.CameraActivity
import com.mamikos.mamiagent.helpers.MediaHelper.createImageFile

import java.io.File

/**
 * Created by Dedi Android on 28/03/2018.
 * Happy coding, buddy!
 */

class ShowCamera(private val mContext: Context) {

    lateinit var fileCamera: File
    lateinit var path: String

    fun showNow(code: Int) {
        takePicture(mContext as Activity, code)
    }

    private fun takePicture(context: Activity, code: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            fileCamera = createImageFile(context)
            val photoURI = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", fileCamera)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        context.startActivityForResult(intent, code)
    }

    companion object {
        var CODE_CAMERA = 63
    }

}

