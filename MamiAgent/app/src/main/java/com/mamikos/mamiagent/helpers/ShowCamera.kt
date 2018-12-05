package com.mamikos.mamiagent.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.widget.Toast
import org.jetbrains.anko.toast

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Dedi Android on 28/03/2018.
 * Happy coding, buddy!
 * https://vlemon.com/blog/android/android-how-to-capture-image-from-camera-and-get-image-save-path-using-kotlin/
 */

class ShowCamera(private val mContext: Context) {

    lateinit var fileCamera: File
    lateinit var path: String

    fun showNow(code: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            takePicture(mContext as Activity, code)
        } else {
            takePicture2(mContext as Activity, code)
        }

    }

    private fun takePicture2(activity: Activity, code: Int) {
        try {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile = createImageFile4()
            if (photoFile != null) {
                val photoURI = Uri.fromFile(photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(cameraIntent, code)
            }else{
                activity.toast("Kamera tidak support")
            }
        } catch (e: Exception) {
            activity.toast("Kamera tidak support")
            e.printStackTrace()
        }
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
                    val photoURI: Uri = FileProvider.getUriForFile(context, "com.mamikos.mamiagent.fileprovider", photoFile)
                    fileCamera = photoFile
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    context.startActivityForResult(takePictureIntent, code)
                } else {
                    context.toast("Kamera not support")
                }
            }
        }
    }

    private fun createImageFile4(): File? {
        // External sdcard location
        val mediaStorageDir = File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "mamiagencamera")
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(mContext, "Gagal membuat folder, mungkin tidak support", Toast.LENGTH_SHORT).show()
                return null
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(Date())

        return File(mediaStorageDir.path + File.separator
                + "IMG_" + timeStamp + ".jpg")

    }


    companion object {
        var CODE_CAMERA = 63
        var CODE_CAMERA_2 = 633
    }

}

