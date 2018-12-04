package com.mamikos.mamiagent.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context.packageManager)?.also {
                val photoFile: File? = try {
                    UtilsHelper.createImageFile(context)
                } catch (ex: Exception) {
                    context.toast("Gagal membuat folder")
                    null
                }
                if (photoFile != null) {
                    val photoURI: Uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                    fileCamera = photoFile
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    context.startActivityForResult(takePictureIntent, code)
                } else {
                    context.toast("Kamera not support")
                }
            }
        }
    }

    companion object {
        var CODE_CAMERA = 63
        var CODE_CAMERA_2 = 633
    }

}

