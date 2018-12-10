package com.mamikos.mamiagent.helpers

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import com.mamikos.mamiagent.BuildConfig
import com.mamikos.mamiagent.R
import android.support.v7.app.AlertDialog
import android.view.View
import com.mamikos.mamiagent.views.LockableScrollView
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import android.provider.MediaStore
import android.support.v4.content.CursorLoader
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and
import android.content.DialogInterface
import android.R.string.yes
import android.app.Activity
import android.app.DatePickerDialog
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import com.mamikos.mamiagent.apps.SessionManager
import com.mamikos.mamiagent.interfaces.OnClickInterfaceObject
import java.io.File
import java.io.IOException


/**
 * Created by Dedi Dot on 9/21/2018.
 * Happy Coding!
 */


class UtilsHelper {

    companion object {

        val KEY_FORMAT_PRICE = "#,###"

        fun log(message: String) {
            if (BuildConfig.SHOW_LOG) {

                val desc = "[" + Exception().stackTrace[1].className + "] " + "[" + Exception().stackTrace[1].lineNumber + "] " + "{" + Exception().stackTrace[1].methodName + "} "

                if (message.length > 500) {
                    try {
                        val maxLogSize = 500
                        for (i in 0..message.length / maxLogSize) {
                            val start = i * maxLogSize
                            var end = (i + 1) * maxLogSize
                            end = if (end > message.length) message.length else end
                            Log.e(":", desc + " => " + message.substring(start, end))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("log Exception", desc + " => " + e.message)
                    }

                } else {
                    Log.e(":", "$desc => $message")
                }
            }
        }

        fun makeHandler(duration: Long, runnable: Runnable) {
            val handler = Handler()
            handler.postDelayed(runnable, duration)
        }

        fun getFontRegular(context: Context): Typeface? {
            return ResourcesCompat.getFont(context, R.font.font_regular)
        }

        fun getFontBold(context: Context): Typeface? {
            return ResourcesCompat.getFont(context, R.font.font_bold)
        }

        fun getFontSemiBold(context: Context): Typeface? {
            return ResourcesCompat.getFont(context, R.font.font_semi_bold)
        }


        fun showDialogYesNo(context: Context, title: String, message: String, runnable: Runnable,
                            icon: Int) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)

            if (!title.isEmpty()) {
                builder.setTitle(title)
            }

            if (icon != 0) {
                builder.setIcon(icon)
            }

            builder.setCancelable(false)
            builder.setPositiveButton(context.getString(R.string.msg_yes)) { dialog, _ ->
                runnable.run()
                dialog.dismiss()
            }
            builder.setNegativeButton(context.getString(R.string.msg_no)) { dialog, _ -> dialog.dismiss() }
            builder.show()

        }

        fun showDialogYes(context: Context, title: String, message: String, runnable: Runnable?,
                          icon: Int) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)

            if (!title.isEmpty()) {
                builder.setTitle(title)
            }

            if (icon != 0) {
                builder.setIcon(icon)
            }

            builder.setCancelable(false)
            builder.setPositiveButton(context.getString(R.string.msg_yes)) { dialog, which ->
                runnable?.run()
                dialog.dismiss()
            }
            builder.show()

        }

        fun autoFocusScroll(view: View, scrollView: LockableScrollView? = null) {
            scrollView?.post {
                scrollView.scrollTo(0, view.top - 100)
            }
        }

        fun showSnackbar(rootView: View, str: String) {
            Snackbar.make(rootView, str, Snackbar.LENGTH_SHORT).show()
        }

        fun getTimestamp(): String {
            val timeStamp = SimpleDateFormat("HH_mm_ss_SSS")
            val now = Date()
            return timeStamp.format(now) + ""
        }

        fun getPathFromURI(context: Context, contentUri: Uri): String? {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            var result: String? = null

            val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
            val cursor = cursorLoader.loadInBackground()
            //log("cek cursorX " + cursor!!) // null di device oppo
            log("cek cursorXX " + cursorLoader.uri.path)

            if (cursor != null) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                result = cursor.getString(column_index)
                cursor.close()
            } else {

                // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    return cursorLoader.uri.path
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    Toast.makeText(context, "getPathSDKBaru null bro", Toast.LENGTH_SHORT).show()
                    return ""
                }

                /*  } else {
                Cursor cursorx = context.getContentResolver().query(contentUri, proj, null, null, null);
                if (cursorx != null) {
                    if (cursorx.moveToFirst()) {
                        int column_index = cursorx.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        result = cursorx.getString(column_index);
                    }
                    cursorx.close();
                }
            }*/
            }

            return result
        }

        fun md5(string: String): String {
            val hash: ByteArray

            try {
                hash = MessageDigest.getInstance("MD5").digest(string.toByteArray(charset("UTF-8")))
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Huh, MD5 should be supported?", e)
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException("Huh, UTF-8 should be supported?", e)
            }

            val hex = StringBuilder(hash.size * 2)

            for (b in hash) {
                val i = b and 0xFF.toByte()
                if (i < 0x10) hex.append('0')
                hex.append(Integer.toHexString(i.toInt()))
            }

            return hex.toString()
        }

        fun hideSoftInput(activity: Activity) {
            var view = activity.currentFocus
            if (view == null) view = View(activity)
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun createFolder(mContext: Context): String {
            val path: String
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                path = Environment.getExternalStorageDirectory().path + "/Android/data/" + mContext.packageName + "/camera/"
            } else {
                path = Environment.getDataDirectory().path + "/Android/data/" + mContext.packageName + "/camera/"
            }
            var dir = File(path)
            if (!(dir.exists() && dir.isDirectory)) {
                dir.mkdirs()
            } else {
                dir = File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "camera")
                if (!dir.exists()) {
                    dir.mkdirs()
                }
            }
            return path
        }


        @Throws(IOException::class) fun createImageFile(context: Context): File {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile("JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */).apply {
                // Save a file: path for use with ACTION_VIEW intents
                val ss = SessionManager(context)
                ss.pathCamera = absolutePath
            }
        }

        fun setupToolbar(context: AppCompatActivity, toolbar: Toolbar, title: String,
                         subtitle: String, icon: Int, actionClick: Runnable) {

            toolbar.title = title

            if (!subtitle.isEmpty()) {
                toolbar.subtitle = subtitle
            }

            if (icon != 0) {
                toolbar.navigationIcon = ContextCompat.getDrawable(context, icon)
            }

            context.setSupportActionBar(toolbar)
            context.supportActionBar?.setDisplayShowHomeEnabled(true)

            toolbar.setNavigationOnClickListener { actionClick.run() }

        }

        fun showDialogYesNoCustomString(context: Context, title: String, message: String,
                                        yes: String, no: String,
                                        runnable: OnClickInterfaceObject<Int>?, icon: Int) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)

            if (!title.isEmpty()) {
                builder.setTitle(title)
            }

            if (icon != 0) {
                builder.setIcon(icon)
            }

            builder.setCancelable(false)
            builder.setPositiveButton(yes) { dialog, _ ->
                runnable?.dataClicked(1)
                dialog.dismiss()
            }
            builder.setNegativeButton(no) { dialog, _ ->
                runnable?.dataClicked(0)
                dialog.dismiss()
            }
            builder.show()

        }

        fun isNetworkConnected(context: Context?): Boolean {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }

        fun showDateDialog(context: Context, clicked: OnClickInterfaceObject<String>) {
            try {
                val newCalendar = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog(context, R.style.DialogDateTheme, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, month, dayOfMonth)
                    val formatAPI = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val birthday = formatAPI.format(newDate.time)
                    clicked.dataClicked(birthday)
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
                datePickerDialog.show()
            } catch (e: Exception) {
                clicked.dataClicked("")
            }
        }

    }
}