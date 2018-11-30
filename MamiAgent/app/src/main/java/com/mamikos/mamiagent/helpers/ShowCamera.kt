package com.mamikos.mamiagent.helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.widget.Toast

import com.mamikos.mamiagent.BuildConfig
import com.mamikos.mamiagent.activity.IntroCheckInActivity

import java.io.File

/**
 * Created by Dedi Android on 28/03/2018.
 * Happy coding, buddy!
 */

class ShowCamera(private val mContext: Context) {

    lateinit var fileCamera: File

    init {
        //createPathCache()
    }

    private val path: String
        get () {
            var path = ""
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                path = Environment.getExternalStorageDirectory().path + "/Android/data/" + mContext.packageName + "/cache/images/"
            } else {
                path = Environment.getDataDirectory().path + "/Android/data/" + mContext.packageName + "/cache/images/"
            } // java.io.FileNotFoundException: /data/user/0/com.mamikos.mamiagent/cache/images (Is a directory)
            val dir = File(path)
            if (!(dir.exists() && dir.isDirectory)) {
                dir.mkdirs()
            }
            return path
        }


    private val pathx: String
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

    fun showNow(code: Int) {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //showNowNougat(code)
        /*} else {
            showNowRegular(code)
        }*/
        launchCamera(mContext as Activity, code)
    }

    private fun showNowRegular(code: Int) {
        val myUri = Uri.parse(path + UtilsHelper.getTimestamp() + ".png")
        fileCamera = File(myUri.path!!)
        val fileUri = Uri.fromFile(fileCamera)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        (mContext as Activity).startActivityForResult(cameraIntent, code)
    }

    private fun showNowNougat(code: Int) {
        try {
            fileCamera = createImageFile()
            val photoURI = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", fileCamera!!)

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            (mContext as Activity).startActivityForResult(cameraIntent, code)

        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

    }

    @Throws(Exception::class)
    private fun createImageFile(): File {
        val myUri = Uri.parse(path + UtilsHelper.getTimestamp() + ".png")
        return File(myUri.path!!)
        //return MediaHelper.createImageFile(mContext)
    }

    var mCurrentPhotoPath = ""
    var photoURI: Uri? = null

    fun launchCamera(context: Activity, code: Int) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.CAMERA), IntroCheckInActivity.SETTING_CAMERA)
            return
        }

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), IntroCheckInActivity.SETTING_CAMERA)
            return
        }

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context.packageManager) != null) {
            // Create the File where the photo should go
            try {
                fileCamera = MediaHelper.createImageFile(context)
                mCurrentPhotoPath = fileCamera.absolutePath
                photoURI = FileProvider.getUriForFile(context, "com.mamikos.mamiagent.provider", fileCamera)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                context.startActivityForResult(takePictureIntent, code)
            } catch (ex: Exception) {
                Toast.makeText(context, "Capture Image Bug: " + ex.message.toString(), Toast.LENGTH_SHORT)
                        .show()
            }
        } else {
            Toast.makeText(context, "null", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        var CODE_CAMERA = 63
    }

}

