package com.mamikos.mamiagent.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.mamikos.mamiagent.BuildConfig
import org.jetbrains.anko.toast

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
            fileCamera = UtilsHelper.createImageFile(context)
            val photoURI = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", fileCamera)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        } catch (e: Exception) {
            e.printStackTrace()
            context.toast("Kamera tidak support")
            return
        }
        context.startActivityForResult(intent, code)
    }

    companion object {
        var CODE_CAMERA = 63
        var CODE_CAMERA_2 = 633
    }

}

