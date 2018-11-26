package com.mamikos.mamiagent.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider

import com.mamikos.mamiagent.BuildConfig

import java.io.File
import java.io.IOException

/**
 * Created by Dedi Android on 28/03/2018.
 * Happy coding, buddy!
 */

class ShowCamera(private val mContext: Context) {

    var fileCamera: File? = null

    private val path: String
        get() {
            val path: String
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                path = Environment.getExternalStorageDirectory().path + "/Android/data/" + mContext.packageName + "/camera/"
            } else {
                path = Environment.getDataDirectory().path + "/Android/data/" + mContext.packageName + "/camera/"
            }
            val dir = File(path)
            if (!(dir.exists() && dir.isDirectory)) {
                dir.mkdirs()
            }
            return path
        }

    fun checkPermission() {
        UtilsPermission.checkPermissionStorageAndCamera(mContext as Activity)
    }

    fun showNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            showNowNougat()
        } else {
            showNowRegular()
        }
    }

    private fun showNowRegular() {
        val myUri = Uri.parse(path + UtilsHelper.getTimestamp() + ".png")
        fileCamera = File(myUri.path!!)
        val fileUri = Uri.fromFile(fileCamera)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        (mContext as Activity).startActivityForResult(cameraIntent, CODE_CAMERA)
    }

    private fun showNowNougat() {
        try {
            fileCamera = createImageFile()
            val photoURI = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", fileCamera!!)

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            val bundle = Bundle()
            bundle.putString("jancook", photoURI.path)

            cameraIntent.putExtras(bundle)
            cameraIntent.putExtra("path_bro", photoURI)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                (mContext as Activity).startActivityForResult(cameraIntent, CODE_CAMERA, bundle)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Throws(Exception::class)
    private fun createImageFile(): File {
        val myUri = Uri.parse(path + UtilsHelper.getTimestamp() + ".png")
        return File(myUri.path!!)
    }

    companion object {
        var CODE_CAMERA = 63
    }

}

